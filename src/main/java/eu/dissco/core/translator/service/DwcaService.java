package eu.dissco.core.translator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.dissco.core.translator.Profiles;
import eu.dissco.core.translator.component.SourceSystemComponent;
import eu.dissco.core.translator.database.jooq.enums.JobState;
import eu.dissco.core.translator.domain.DigitalMediaEvent;
import eu.dissco.core.translator.domain.DigitalMediaWrapper;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.domain.DigitalSpecimenWrapper;
import eu.dissco.core.translator.domain.Enrichment;
import eu.dissco.core.translator.domain.TranslatorJobResult;
import eu.dissco.core.translator.exception.DiSSCoDataException;
import eu.dissco.core.translator.exception.DisscoRepositoryException;
import eu.dissco.core.translator.exception.OrganisationException;
import eu.dissco.core.translator.exception.ReachedMaximumLimitException;
import eu.dissco.core.translator.properties.ApplicationProperties;
import eu.dissco.core.translator.properties.DwcaProperties;
import eu.dissco.core.translator.properties.EnrichmentProperties;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.repository.DwcaRepository;
import eu.dissco.core.translator.terms.BaseDigitalObjectDirector;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.gbif.dwc.Archive;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.DwcFiles;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.DwcaTerm;
import org.gbif.dwc.terms.Term;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@Profile(Profiles.DWCA)
@RequiredArgsConstructor
public class DwcaService extends WebClientService {

  private static final String DWC_ASSOCIATED_MEDIA = "dwc:associatedMedia";
  private static final String GBIF_MULTIMEDIA = "gbif:Multimedia";
  private static final String EML_LICENSE = "eml:license";
  private static final String AC_MULTIMEDIA = "http://rs.tdwg.org/ac/terms/Multimedia";
  private static final String EXTENSIONS = "extensions";

  private final ObjectMapper mapper;
  private final WebClient webClient;
  private final DwcaProperties dwcaProperties;
  private final RabbitMqService rabbitMqService;
  private final EnrichmentProperties enrichmentProperties;
  private final SourceSystemComponent sourceSystemComponent;
  private final DwcaRepository dwcaRepository;
  private final BaseDigitalObjectDirector digitalSpecimenDirector;
  private final FdoProperties fdoProperties;
  private final ApplicationProperties applicationProperties;
  private final XMLInputFactory xmlFactory;
  private final List<String> allowedBasisOfRecord = List.of("PRESERVEDSPECIMEN", "FOSSIL", "OTHER",
      "ROCK", "MINERAL", "METEORITE", "FOSSILSPECIMEN", "LIVINGSPECIMEN", "MATERIALSAMPLE", "PRESERVED_SPECIMEN");

  @Override
  public TranslatorJobResult retrieveData() {
    var endpoint = sourceSystemComponent.getSourceSystemEndpoint();
    Archive archive = null;
    var processedRecords = new AtomicInteger(0);
    try {
      var file = Path.of(dwcaProperties.getDownloadFile());
      var buffer = webClient.get().uri(URI.create(endpoint)).retrieve()
          .bodyToFlux(DataBuffer.class);
      DataBufferUtils.write(buffer, Files.newOutputStream(file)).map(DataBufferUtils::release)
          .then().toFuture().get();
      archive = DwcFiles.fromCompressed(file, Path.of(dwcaProperties.getTempFolder()));
      var ids = postArchiveToDatabase(archive);
      getSpecimenData(ids, archive, processedRecords);
    } catch (IOException e) {
      log.error("Failed to open output stream for download file", e);
      return new TranslatorJobResult(JobState.FAILED, processedRecords.get());
    } catch (ExecutionException e) {
      log.error("Failed during downloading file with exception", e.getCause());
      return new TranslatorJobResult(JobState.FAILED, processedRecords.get());
    } catch (InterruptedException e) {
      log.error("Failed during downloading file due to interruption", e);
      Thread.currentThread().interrupt();
      return new TranslatorJobResult(JobState.FAILED, processedRecords.get());
    } catch (DisscoRepositoryException e) {
      log.error("Failed during batch copy into temp tables with exception", e);
      return new TranslatorJobResult(JobState.FAILED, processedRecords.get());
    } finally {
      if (archive != null) {
        log.info("Cleaning up database tables");
        removeTempTables(archive);
      }
    }
    if (processedRecords.get() == 0) {
      log.info("No records were successfully processed");
      return new TranslatorJobResult(JobState.FAILED, processedRecords.get());
    }
    return new TranslatorJobResult(JobState.COMPLETED, processedRecords.get());
  }

  public void getSpecimenData(Set<String> ids, Archive archive, AtomicInteger processedRecords)
      throws IOException {
    var batches = prepareChunks(ids, 10000);
    sourceSystemComponent.storeEmlRecord(archive.getMetadataLocationFile());
    var optionalEmlData = addDatasetMeta(archive.getMetadataLocationFile());
    try {
      for (var batch : batches) {
        log.info("Starting to get records for: core");
        var specimenData = dwcaRepository.getCoreRecords(batch, getTableName(archive.getCore(), true));
        log.info("Got specimen batch: {}", batch.size());
        addExtensionsToSpecimen(archive, batch, specimenData);
        log.info("Start translation and publishing of batch: {}", specimenData.size());
        processDigitalSpecimen(specimenData.values(), optionalEmlData, processedRecords);
      }
    } catch (ReachedMaximumLimitException e) {
      log.warn("Reached maximum limit of {} processed specimens out of {} specimens available",
          applicationProperties.getMaxItems(), ids.size());
      return;
    }
    log.info("Finished processing all {} specimens available", ids.size());
  }

  private void addExtensionsToSpecimen(Archive archive, List<String> batch,
      Map<String, ObjectNode> specimenData) {
    for (var extension : archive.getExtensions()) {
      log.info("Starting to get records for: {} ", getTableName(extension, false));
      var extensionData = dwcaRepository.getRecords(batch, getTableName(extension, false));
      log.info("Got {} with records: {} ", getTableName(extension, false), extensionData.size());
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

  private void processDigitalSpecimen(Collection<ObjectNode> fullRecords,
      Optional<Map<String, String>> optionalEmlData, AtomicInteger processedRecords)
      throws JsonProcessingException, ReachedMaximumLimitException {
    for (var fullRecord : fullRecords) {
      if (fullRecord != null) {
        if (applicationProperties.getMaxItems() != null
            && processedRecords.get() >= applicationProperties.getMaxItems()) {
          throw new ReachedMaximumLimitException();
        }
        try {
          optionalEmlData.ifPresent(emlData -> addEmlDataToRecord(fullRecord, emlData));
          var digitalObjects = createDigitalObjects(fullRecord);
          log.debug("Digital Specimen: {}", digitalObjects);
          var translatorEvent = new DigitalSpecimenEvent(enrichmentServices(false),
              digitalObjects.getLeft(), digitalObjects.getRight());
          rabbitMqService.sendMessage(translatorEvent);
          processedRecords.incrementAndGet();
        } catch (DiSSCoDataException e) {
          log.error("Encountered data issue with record: {}", fullRecord, e);
        }
      }
    }
  }

  private void retrieveTitle(XMLEventReader xmlEventReader, HashMap<String, String> emlData)
      throws XMLStreamException {
    var title = xmlEventReader.nextEvent().asCharacters().getData();
    if (title != null) {
      log.debug("Found the dataset title in eml: {}", title);
      emlData.put("eml:title", title);
    }
  }

  private void addEmlDataToRecord(ObjectNode fullRecord, Map<String, String> emlData) {
    for (var entry : emlData.entrySet()) {
      fullRecord.put(entry.getKey(), entry.getValue());
    }
  }

  private Optional<Map<String, String>> addDatasetMeta(File mf) {
    var emlData = new HashMap<String, String>();
    try {
      var xmlEventReader = xmlFactory.createXMLEventReader(new FileInputStream(mf));
      while (xmlEventReader.hasNext()) {
        retrieveEmlData(xmlEventReader, emlData);
      }
    } catch (FileNotFoundException e) {
      log.error("Unable to find EML file for dataset at location: {}", mf.getAbsolutePath());
    } catch (XMLStreamException e) {
      log.error("Unable to process EML", e);
    }
    return emlData.isEmpty() ? Optional.empty() : Optional.of(emlData);
  }

  private void retrieveEmlData(XMLEventReader xmlEventReader, HashMap<String, String> emlData)
      throws XMLStreamException {
    var element = xmlEventReader.nextEvent();
    if (isStartElement(element, "title")) {
      retrieveTitle(xmlEventReader, emlData);
    }
    if (isStartElement(element, "intellectualRights")) {
      var license = retrieveLicense(xmlEventReader);
      if (license != null) {
        emlData.put(EML_LICENSE, license);
      }
    }
  }

  private String retrieveLicense(XMLEventReader xmlEventReader) throws XMLStreamException {
    while (xmlEventReader.hasNext()) {
      var element = xmlEventReader.nextEvent();
      if (isStartElement(element, "ulink")) {
        var attribute = element.asStartElement().getAttributeByName(QName.valueOf("url"));
        if (attribute != null) {
          var url = attribute.getValue();
          log.debug("Found the license url in eml: {}", url);
          return url;
        }
      } else if (isStartElement(element, "citetitle")) {
        var title = xmlEventReader.nextEvent().asCharacters().getData();
        log.debug("Found license title in eml: {}", title);
        return title;
      } else if (isStartElement(element, "pubDate")) {
        return "";
      }
    }
    return "";
  }

  private List<DigitalMediaEvent> processMedia(String recordId, JsonNode fullDigitalSpecimen,
      String organisationId) throws OrganisationException {
    var extensions = fullDigitalSpecimen.get(EXTENSIONS);
    if (extensions != null) {
      if (extensions.get(AC_MULTIMEDIA) != null) {
        var imageArray = extensions.get(AC_MULTIMEDIA);
        addDatasetMetadata(imageArray, fullDigitalSpecimen);
        if (imageArray.isArray() && !imageArray.isEmpty()) {
          return extractMultiMedia(recordId, imageArray, organisationId);
        }
      } else if (extensions.get(GBIF_MULTIMEDIA) != null) {
        var imageArray = extensions.get(GBIF_MULTIMEDIA);
        addDatasetMetadata(imageArray, fullDigitalSpecimen);
        if (imageArray.isArray() && !imageArray.isEmpty()) {
          return extractMultiMedia(recordId, imageArray, organisationId);
        }
      }
    } if (fullDigitalSpecimen.get(DWC_ASSOCIATED_MEDIA) != null) {
      return publishAssociatedMedia(recordId,
          fullDigitalSpecimen.get(DWC_ASSOCIATED_MEDIA).asText(), organisationId,
          fullDigitalSpecimen.get(EML_LICENSE));
    }
    return List.of();
  }

  private void addDatasetMetadata(JsonNode imageArray, JsonNode fullDigitalSpecimen) {
    for (JsonNode jsonNode : imageArray) {
      var imageNode = (ObjectNode) jsonNode;
      imageNode.set(EML_LICENSE, fullDigitalSpecimen.get(EML_LICENSE));
    }

  }

  private List<DigitalMediaEvent> extractMultiMedia(String recordId, JsonNode imageArray,
      String organisationId) {
    var digitalMediaEvents = new ArrayList<DigitalMediaEvent>();
    for (var image : imageArray) {
      try {
        var digitalMedia = digitalSpecimenDirector.assembleDigitalMedia(true, image,
            organisationId);
        if (digitalMedia.getAcAccessURI() == null) {
          throw new DiSSCoDataException(
              "Digital media object for specimen does not have an access uri, ignoring record");
        }
        var digitalMediaEvent = new DigitalMediaEvent(enrichmentServices(true),
            new DigitalMediaWrapper(
                fdoProperties.getDigitalMediaType(),
                digitalMedia,
                image));
        digitalMediaEvents.add(digitalMediaEvent);
      } catch (DiSSCoDataException e) {
        log.error("Failed to process digital media object for digital specimen: {}",
            recordId, e);
      }
    }
    return digitalMediaEvents;
  }

  private List<DigitalMediaEvent> publishAssociatedMedia(String recordId,
      String associatedMedia, String organisationId, JsonNode licenseNode)
      throws OrganisationException {
    log.debug("Digital Specimen: {}, has associatedMedia {}", recordId,
        associatedMedia);
    var mediaUrls = new LinkedHashSet<>(Arrays.asList(associatedMedia.split("\\|")));
    var digitalMedia = new ArrayList<DigitalMediaEvent>();
    for (var mediaUrl : mediaUrls) {
      var digitalMediaEvent = new DigitalMediaEvent(enrichmentServices(true),
          new DigitalMediaWrapper(
              fdoProperties.getDigitalMediaType(),
              digitalSpecimenDirector.assembleDigitalMedia(true,
                  mapper.createObjectNode().put("ac:accessURI", mediaUrl)
                      .set(EML_LICENSE, licenseNode),
                  organisationId),
              null));
      digitalMedia.add(digitalMediaEvent);
    }
    return digitalMedia;
  }

  private Pair<DigitalSpecimenWrapper, List<DigitalMediaEvent>> createDigitalObjects(
      JsonNode fullRecord) throws DiSSCoDataException {
    var ds = digitalSpecimenDirector.assembleDigitalSpecimenTerm(fullRecord, true);
    if (ds.getOdsNormalisedPhysicalSpecimenID() == null || ds.getOdsOrganisationID() == null) {
      throw new DiSSCoDataException(
          "Record does not comply to MIDS level 0 (id and organisation), ignoring record");
    }
    return Pair.of(new DigitalSpecimenWrapper(
            ds.getOdsNormalisedPhysicalSpecimenID(),
            fdoProperties.getDigitalSpecimenType(),
            ds,
            cleanupRedundantFields(fullRecord)),
        processMedia(ds.getOdsNormalisedPhysicalSpecimenID(), fullRecord, ds.getOdsOrganisationID())
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

  private Collection<List<String>> prepareChunks(Set<String> inputList, int chunkSize) {
    AtomicInteger counter = new AtomicInteger();
    return inputList.stream()
        .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize)).values();
  }


  private Set<String> postArchiveToDatabase(Archive archive) throws DisscoRepositoryException {
    var tableNames = generateTableNames(archive);
    createTempTables(tableNames);
    log.info("Created tables: {}", tableNames);
    var idList = postCore(archive.getCore());
    postExtensions(archive.getExtensions(), idList);
    return idList;
  }

  private void removeTempTables(Archive archive) {
    var tableNames = generateTableNames(archive);
    for (var tableName : tableNames) {
      dwcaRepository.deleteTable(tableName.getLeft());
    }
  }

  private List<Pair<String, Term>> generateTableNames(Archive archive) {
    var list = new ArrayList<Pair<String, Term>>();
    list.add(Pair.of(getTableName(archive.getCore(), true), archive.getCore().getRowType()));
    for (var extension : archive.getExtensions()) {
      list.add(Pair.of(getTableName(extension, false), extension.getRowType()));
    }
    return list;
  }

  private String getTableName(ArchiveFile archiveFile, boolean isCore) {
    var fullSourceSystemId = sourceSystemComponent.getSourceSystemID();
    var tableType= isCore? "_core_" : "_extension_";
    var minifiedSourceSystemId = fullSourceSystemId.substring(fullSourceSystemId.indexOf('/') + 1)
        .replace("-", "_");
    var tableName = "temp" +  tableType + (minifiedSourceSystemId + "_" + archiveFile.getRowType().simpleName()).toLowerCase()
        .replace(":", "_");
    tableName = tableName.replace("/", "_");
    return tableName.replace(".", "_");
  }

  private void createTempTables(List<Pair<String, Term>> tableNames) {
    for (var tableName : tableNames) {
      dwcaRepository.createTable(tableName.getLeft(), tableName.getRight());
    }
  }

  private Set<String> postCore(ArchiveFile core) {
    var dbRecords = new ArrayList<Pair<String, JsonNode>>();
    var idList = new HashSet<String>();
    for (var rec : core) {
      var basisOfRecord = rec.value(DwcTerm.basisOfRecord);
      if (basisOfRecord != null && allowedBasisOfRecord.contains(basisOfRecord.toUpperCase())) {
        idList.add(rec.id());
        var json = convertToJson(core, rec);
        json.set(EXTENSIONS, mapper.createObjectNode());
        dbRecords.add(Pair.of(rec.id(), json));
        if (dbRecords.size() % 10000 == 0) {
          postToDatabase(core, dbRecords, true);
        }
      } else {
        log.debug("Record with id: {} has basisOfRecord: {} which is not an accepted basisOfRecord",
            rec.id(), basisOfRecord);
      }
    }
    if (!dbRecords.isEmpty()) {
      postToDatabase(core, dbRecords, true);
    }
    log.info("Finished posting core archive to database, total records: {}", idList.size());
    return idList;
  }

  private void postToDatabase(ArchiveFile archiveFile,
      ArrayList<Pair<String, JsonNode>> dbRecords, boolean isCore) {
    log.info("Persisting {} records to database", dbRecords.size());
    dwcaRepository.postRecords(getTableName(archiveFile, isCore), archiveFile.getRowType(), dbRecords);
    dbRecords.clear();
  }

  private void postExtensions(Set<ArchiveFile> extensions, Set<String> idsList) {
    var dbRecords = new ArrayList<Pair<String, JsonNode>>();
    for (var extension : extensions) {
      log.info("Processing records of extension: {}", extension.getRowType().toString());
      for (var rec : extension) {
        if (idsList.contains(rec.id())) {
          dbRecords.add(Pair.of(rec.id(), convertToJson(extension, rec)));
          if (dbRecords.size() % 10000 == 0) {
            postToDatabase(extension, dbRecords, false);
          }
        }
      }
      if (!dbRecords.isEmpty()) {
        postToDatabase(extension, dbRecords, false);
      }
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
