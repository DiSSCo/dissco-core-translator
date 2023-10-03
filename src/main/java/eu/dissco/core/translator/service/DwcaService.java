package eu.dissco.core.translator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.dissco.core.translator.Profiles;
import eu.dissco.core.translator.domain.DigitalMediaObject;
import eu.dissco.core.translator.domain.DigitalMediaObjectEvent;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.domain.DigitalSpecimenWrapper;
import eu.dissco.core.translator.domain.Enrichment;
import eu.dissco.core.translator.exception.DiSSCoDataException;
import eu.dissco.core.translator.exception.OrganisationNotRorId;
import eu.dissco.core.translator.properties.DwcaProperties;
import eu.dissco.core.translator.properties.EnrichmentProperties;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.DwcaRepository;
import eu.dissco.core.translator.repository.SourceSystemRepository;
import eu.dissco.core.translator.terms.DigitalObjectDirector;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.gbif.dwc.Archive;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.DwcFiles;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.DwcaTerm;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@Profile(Profiles.DWCA)
@RequiredArgsConstructor
public class DwcaService implements WebClientService {

  private static final String DWC_ASSOCIATED_MEDIA = "dwc:associatedMedia";
  private static final String GBIF_MULTIMEDIA = "gbif:Multimedia";
  private static final String AC_MULTIMEDIA = "http://rs.tdwg.org/ac/terms/Multimedia";
  private static final String UNKNOWN = "Unknown";
  private static final String EXTENSIONS = "extensions";

  private final ObjectMapper mapper;
  private final WebClientProperties webClientProperties;
  private final WebClient webClient;
  private final DwcaProperties dwcaProperties;
  private final KafkaService kafkaService;
  private final EnrichmentProperties enrichmentProperties;
  private final SourceSystemRepository repository;
  private final DwcaRepository dwcaRepository;
  private final DigitalObjectDirector digitalSpecimenDirector;
  private final FdoProperties fdoProperties;
  private final List<String> allowedBasisOfRecord = List.of("PRESERVEDSPECIMEN", "FOSSIL", "OTHER",
      "ROCK", "MINERAL", "METEORITE", "FOSSILSPECIMEN", "LIVINGSPECIMEN", "MATERIALSAMPLE");

  @Override
  public void retrieveData() {
    var endpoint = repository.getEndpoint(webClientProperties.getSourceSystemId());
    Archive archive = null;
    try {
      var file = Path.of(dwcaProperties.getDownloadFile());
      var buffer = webClient.get().uri(URI.create(endpoint)).retrieve()
          .bodyToFlux(DataBuffer.class);
      DataBufferUtils.write(buffer, Files.newOutputStream(file)).map(DataBufferUtils::release)
          .then().toFuture().get();
      archive = DwcFiles.fromCompressed(file, Path.of(dwcaProperties.getTempFolder()));
      var ids = postArchiveToDatabase(archive);
      getSpecimenData(ids, archive);
    } catch (IOException e) {
      log.error("Failed to open output stream for download file", e);
    } catch (ExecutionException e) {
      log.error("Failed during downloading file with exception", e.getCause());
    } catch (InterruptedException e) {
      log.error("Failed during downloading file due to interruption", e);
      Thread.currentThread().interrupt();
    } finally {
      if (archive != null) {
        log.info("Cleaning up database tables");
        removeTempTables(archive);
      }
    }
  }

  public void getSpecimenData(List<String> ids, Archive archive) throws JsonProcessingException {
    var batches = prepareChunks(ids, 10000);
    for (var batch : batches) {
      log.info("Starting to get records for: core");
      var specimenData = dwcaRepository.getCoreRecords(batch, getTableName(archive.getCore()));
      log.info("Got specimen batch: {}", batch.size());
      addExtensionsToSpecimen(archive, batch, specimenData);
      log.info("Start translation and publishing of batch: {}", specimenData.values().size());
      processDigitalSpecimen(specimenData.values());
    }
    log.info("Finished processing");
  }

  private void addExtensionsToSpecimen(Archive archive, List<String> batch,
      Map<String, ObjectNode> specimenData) {
    for (var extension : archive.getExtensions()) {
      log.info("Starting to get records for: {} ", getTableName(extension));
      var extensionData = dwcaRepository.getRecords(batch, getTableName(extension));
      log.info("Got {} with records: {} ", getTableName(extension), extensionData.size());
      for (var specimenValue : specimenData.entrySet()) {
        var extensionValue = extensionData.get(specimenValue.getKey());
        if (extensionValue != null && specimenValue.getValue() != null) {
          var jsonArrayNode = mapper.createArrayNode();
          for (var recordValue : extensionValue) {
            jsonArrayNode.add(recordValue);
          }
          var extensionArray = (ObjectNode) specimenValue.getValue().get(EXTENSIONS);
          extensionArray.set(extension.getRowType().prefixedName(), jsonArrayNode);
        }
      }
    }
  }

  private void processDigitalSpecimen(Collection<ObjectNode> fullRecords)
      throws JsonProcessingException {
    for (var fullRecord : fullRecords) {
      if (fullRecord != null) {
        var recordId = fullRecord.get(DwcaTerm.ID.prefixedName()).asText();
        if (!recordNeedsToBeIgnored(fullRecord, recordId)) {
          try {
            var digitalObjects = createDigitalObjects(fullRecord);
            log.debug("Digital Specimen: {}", digitalObjects);
            var translatorEvent = new DigitalSpecimenEvent(enrichmentServices(false),
                digitalObjects.getLeft(), digitalObjects.getRight());
            kafkaService.sendMessage("digital-specimen",
                mapper.writeValueAsString(translatorEvent));
          } catch (DiSSCoDataException e) {
            log.error("Encountered data issue with record: {}", fullRecord, e);
          }
        }
      }
    }
  }

  private List<DigitalMediaObjectEvent> processMedia(String recordId, JsonNode fullDigitalSpecimen,
      String organisationId) throws OrganisationNotRorId {
    var extensions = fullDigitalSpecimen.get(EXTENSIONS);
    if (extensions != null) {
      if (extensions.get(AC_MULTIMEDIA) != null) {
        var imageArray = extensions.get(AC_MULTIMEDIA);
        if (imageArray.isArray() && !imageArray.isEmpty()) {
          return extractMultiMedia(recordId, imageArray, organisationId);
        }
      } else if (extensions.get(GBIF_MULTIMEDIA) != null) {
        var imageArray = extensions.get(GBIF_MULTIMEDIA);
        if (imageArray.isArray() && !imageArray.isEmpty()) {
          return extractMultiMedia(recordId, imageArray, organisationId);
        }
      }
    } else if (fullDigitalSpecimen.get(DWC_ASSOCIATED_MEDIA) != null) {
      return publishAssociatedMedia(recordId,
          fullDigitalSpecimen.get(DWC_ASSOCIATED_MEDIA).asText(), organisationId);
    }
    return List.of();
  }

  private List<DigitalMediaObjectEvent> extractMultiMedia(String recordId, JsonNode imageArray,
      String organisationId) throws OrganisationNotRorId {
    var digitalMediaObjectEvents = new ArrayList<DigitalMediaObjectEvent>();
    for (var image : imageArray) {
      var digitalMediaObject = new DigitalMediaObjectEvent(enrichmentServices(true),
          new DigitalMediaObject(
              fdoProperties.getDigitalMediaObjectType(),
              recordId,
              digitalSpecimenDirector.assembleDigitalMediaObjects(true, image, organisationId),
              image));
      digitalMediaObjectEvents.add(digitalMediaObject);
    }
    return digitalMediaObjectEvents;
  }

  private List<DigitalMediaObjectEvent> publishAssociatedMedia(String recordId,
      String associatedMedia,
      String organisationId) throws OrganisationNotRorId {
    log.debug("Digital Specimen: {}, has associatedMedia {}", recordId,
        associatedMedia);
    String[] mediaUrls = associatedMedia.split("\\|");
    var digitalMediaObjects = new ArrayList<DigitalMediaObjectEvent>();
    for (var mediaUrl : mediaUrls) {
      var digitalMediaObject = new DigitalMediaObjectEvent(enrichmentServices(true),
          new DigitalMediaObject(
              fdoProperties.getDigitalMediaObjectType(),
              recordId,
              digitalSpecimenDirector.assembleDigitalMediaObjects(true,
                  mapper.valueToTree(mediaUrl),
                  organisationId),
              null));
      digitalMediaObjects.add(digitalMediaObject);
    }
    return digitalMediaObjects;
  }

  private Pair<DigitalSpecimenWrapper, List<DigitalMediaObjectEvent>> createDigitalObjects(
      JsonNode fullRecord) throws DiSSCoDataException {
    var ds = digitalSpecimenDirector.assembleDigitalSpecimenTerm(fullRecord, true);
    return Pair.of(new DigitalSpecimenWrapper(
            ds.getOdsNormalisedPhysicalSpecimenId(),
            fdoProperties.getDigitalSpecimenType(),
            ds,
            cleanupRedundantFields(fullRecord)),
        processMedia(ds.getOdsNormalisedPhysicalSpecimenId(), fullRecord, ds.getDwcInstitutionId())
    );
  }

  private JsonNode cleanupRedundantFields(JsonNode fullRecord) {
    var originalData = (ObjectNode) fullRecord.deepCopy();
    originalData.remove("ods:taxonIdentificationIndex");
    ObjectNode extensions = (ObjectNode) originalData.get(EXTENSIONS);
    if (extensions != null) {
      extensions.remove(List.of(GBIF_MULTIMEDIA, AC_MULTIMEDIA));
    }
    return originalData;
  }

  private boolean recordNeedsToBeIgnored(JsonNode fullRecord, String recordId) {
    var basisOfRecord = fullRecord.get(DwcTerm.basisOfRecord.prefixedName());
    if (basisOfRecord == null) {
      log.warn("Record with id: {} is missing the basis of Record, Record will be ignored",
          recordId);
      return true;
    } else {
      if (!basisOfRecord.isTextual()) {
        log.warn("Record with id: {} has basis of Record which is not a text field: {}",
            recordId, basisOfRecord);
        return true;
      }
      var value = basisOfRecord.asText().toUpperCase();
      if (allowedBasisOfRecord.contains(value)) {
        return false;
      } else {
        log.warn("Record with id: {} has basisOfRecord: {} which is not a physical specimen",
            recordId, basisOfRecord);
        return true;
      }
    }
  }

  private Collection<List<String>> prepareChunks(List<String> inputList, int chunkSize) {
    AtomicInteger counter = new AtomicInteger();
    return inputList.stream()
        .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize)).values();
  }


  private List<String> postArchiveToDatabase(Archive archive) {
    var tableNames = generateTableNames(archive);
    createTempTables(tableNames);
    log.info("Created tables: {}", tableNames);
    var idList = postCore(archive.getCore());
    postExtensions(archive.getExtensions());
    return idList;

  }

  private void removeTempTables(Archive archive) {
    var tableNames = generateTableNames(archive);
    for (var tableName : tableNames) {
      dwcaRepository.deleteTable(tableName);
    }
  }

  private List<String> generateTableNames(Archive archive) {
    var list = new ArrayList<String>();
    list.add(getTableName(archive.getCore()));
    for (var extension : archive.getExtensions()) {
      list.add(getTableName(extension));
    }
    return list;
  }

  private String getTableName(ArchiveFile archiveFile) {
    var fullSourceSystemId = webClientProperties.getSourceSystemId();
    var minifiedSourceSystemId = fullSourceSystemId.substring(fullSourceSystemId.indexOf('/') + 1);
    return minifiedSourceSystemId + "_" + archiveFile.getRowType().prefixedName();
  }

  private void createTempTables(List<String> tableNames) {
    for (var tableName : tableNames) {
      dwcaRepository.createTable(tableName);
    }
  }

  private ArrayList<String> postCore(ArchiveFile core) {
    var dbRecords = new ArrayList<Pair<String, JsonNode>>();
    var idList = new ArrayList<String>();
    for (var rec : core) {
      idList.add(rec.id());
      var json = convertToJson(core, rec);
      json.set(EXTENSIONS, mapper.createObjectNode());
      dbRecords.add(Pair.of(rec.id(), json));
      if (dbRecords.size() % 10000 == 0) {
        postToDatabase(core, dbRecords);
      }
    }
    postToDatabase(core, dbRecords);
    log.info("Finished posting core archive to database");
    return idList;
  }

  private void postToDatabase(ArchiveFile archiveFile,
      ArrayList<Pair<String, JsonNode>> dbRecords) {
    log.info("Persisting {} records to database", dbRecords.size());
    dwcaRepository.postRecords(getTableName(archiveFile), dbRecords);
    dbRecords.clear();
  }


  private void postExtensions(Set<ArchiveFile> extensions) {
    var dbRecords = new ArrayList<Pair<String, JsonNode>>();
    for (var extension : extensions) {
      for (var rec : extension) {
        dbRecords.add(Pair.of(rec.id(), convertToJson(extension, rec)));
        if (dbRecords.size() % 10000 == 0) {
          postToDatabase(extension, dbRecords);
        }
      }
      postToDatabase(extension, dbRecords);
    }
    log.info("Finished posting extensions archive to database");
  }

  private List<String> enrichmentServices(boolean multiMediaObject) {
    if (enrichmentProperties.getList() != null) {
      return enrichmentProperties.getList().stream()
          .filter(e -> e.isImageOnly() == multiMediaObject).map(Enrichment::getName).toList();
    } else {
      return Collections.emptyList();
    }
  }

  private ObjectNode convertToJson(ArchiveFile core, Record rec) {
    var data = mapper.createObjectNode();
    data.put(DwcaTerm.ID.prefixedName(), rec.id());
    for (var field : core.getFields().keySet()) {
      if (rec.value(field) != null) {
        data.put(field.prefixedName(), rec.value(field));
      }
    }
    return data;
  }

}
