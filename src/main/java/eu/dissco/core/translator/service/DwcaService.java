package eu.dissco.core.translator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.Profiles;
import eu.dissco.core.translator.component.MappingComponent;
import eu.dissco.core.translator.domain.DigitalMediaObject;
import eu.dissco.core.translator.domain.DigitalMediaObjectEvent;
import eu.dissco.core.translator.domain.DigitalSpecimen;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.domain.Enrichment;
import eu.dissco.core.translator.properties.DwcaProperties;
import eu.dissco.core.translator.properties.EnrichmentProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.SourceSystemRepository;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gbif.dwc.Archive;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.DwcFiles;
import org.gbif.dwc.record.Record;
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


  private static final String DCTERMS_FORMAT = "dcterms:format";
  private static final String DWC_ASSOCIATED_MEDIA = "dwc:associatedMedia";
  private static final String UNKNOWN = "Unknown";
  private static final String PHYSICAL_SPECIMEN_ID = "physical_specimen_id";
  private static final List<String> DEFAULT_MAPPED_FIELD = List.of("ac:accessUri", DCTERMS_FORMAT,
      "dc:type", "ac:accessURI", "dcterms:type", DWC_ASSOCIATED_MEDIA, "dcterms:identifier");

  private final ObjectMapper mapper;
  private final WebClientProperties webClientProperties;
  private final WebClient webClient;
  private final DwcaProperties dwcaProperties;
  private final KafkaService kafkaService;
  private final MappingComponent mapping;
  private final EnrichmentProperties enrichmentProperties;
  private final SourceSystemRepository repository;
  private final List<String> idTobeIgnored = new ArrayList<>();
  private final List<String> allowedBasisOfRecord = List.of("PRESERVEDSPECIMEN", "FOSSIL", "OTHER",
      "ROCK", "MINERAL", "METEORITE", "FOSSILSPECIMEN", "LIVINGSPECIMEN", "MATERIALSAMPLE");

  @Override
  public void retrieveData() {
    var endpoint = repository.getEndpoint(webClientProperties.getSourceSystemId());
    try {
      var file = Path.of(dwcaProperties.getDownloadFile());
      var buffer = webClient.get().uri(URI.create(endpoint)).retrieve()
          .bodyToFlux(DataBuffer.class);
      DataBufferUtils.write(buffer, Files.newOutputStream(file)).map(DataBufferUtils::release)
          .then().toFuture().get();
      var archive = DwcFiles.fromCompressed(file, Path.of(dwcaProperties.getTempFolder()));
      processDigitalSpecimen(archive.getCore());
      processDigitalMedia(archive);
    } catch (IOException e) {
      log.error("Failed to open output stream for download file", e);
    } catch (ExecutionException e) {
      log.error("Failed during downloading file with exception", e.getCause());
    } catch (InterruptedException e) {
      log.error("Failed during downloading file due to interruption", e);
      Thread.currentThread().interrupt();
    }
  }

  private void processDigitalMedia(Archive archive) throws JsonProcessingException {
    for (var extension : archive.getExtensions()) {
      if (extension.getRowType().qualifiedName().equals("http://rs.tdwg.org/ac/terms/Multimedia")) {
        log.info("Title: {}", extension.getRowType().prefixedName());
        extractACMultimedia(extension);
      }
      if (extension.getRowType().qualifiedName()
          .equals("http://rs.gbif.org/terms/1.0/Multimedia")) {
        log.info("Title: {}", extension.getRowType().prefixedName());
        extractGbifMultiMedia(extension);
      }
    }
  }

  private void extractGbifMultiMedia(ArchiveFile extension) throws JsonProcessingException {
    for (var rec : extension) {
      var type = determineDigitalMediaType(extension, rec);
      log.info("Type of digitalMediaObject is: {}", type);
      var digitalMediaObject = new DigitalMediaObject(type, rec.id(),
          rec.value(extension.getField("dcterms:identifier").getTerm()),
          rec.value(extension.getField(DCTERMS_FORMAT).getTerm()),
          webClientProperties.getSourceSystemId(), getData(extension, rec, false),
          getData(extension, rec, true), "DWCA");
      publishDigitalMediaObject(digitalMediaObject);
    }
  }

  private void extractACMultimedia(ArchiveFile extension) throws JsonProcessingException {
    for (var rec : extension) {
      if (idTobeIgnored.contains(rec.id())) {
        log.info("Skipped media with id: {} as the specimen has been ignored", rec.id());
      } else {
        var type = determineDigitalMediaType(extension, rec);
        log.info("Type of digitalMediaObject is: {}", type);
        var digitalMediaObject = new DigitalMediaObject(
            type,
            rec.id(),
            rec.value(extension.getField("ac:accessUri").getTerm()),
            rec.value(extension.getField(DCTERMS_FORMAT).getTerm()),
            webClientProperties.getSourceSystemId(), getData(extension, rec, false),
            getData(extension, rec, true),
            "DWCA");
        publishDigitalMediaObject(digitalMediaObject);
      }
    }
  }

  private void publishDigitalMediaObject(DigitalMediaObject digitalMediaObject)
      throws JsonProcessingException {
    log.info("MultiMediaObject: {}", digitalMediaObject);
    var digitalMediaObjectEvent = new DigitalMediaObjectEvent(enrichmentServices(true),
        digitalMediaObject);
    kafkaService.sendMessage("digital-media-object",
        mapper.writeValueAsString(digitalMediaObjectEvent));
  }

  private List<String> enrichmentServices(boolean multiMediaObject) {
    if (enrichmentProperties.getList() != null) {
      return enrichmentProperties.getList().stream()
          .filter(e -> e.isImageOnly() == multiMediaObject).map(Enrichment::getName).toList();
    } else {
      return Collections.emptyList();
    }
  }

  private String determineDigitalMediaType(ArchiveFile extension, Record rec) {
    if (getTypeField(extension) == null) {
      log.warn("No dc or dcterms type available in extension with id {}", rec.id());
      return UNKNOWN;
    }
    var dublinCoreType = rec.value(getTypeField(extension).getTerm());
    if (dublinCoreType.equals("StillImage")) {
      return "2DImageObject";
    }
    log.warn("Received Unmapped type from the dwca");
    return UNKNOWN;
  }

  private ArchiveField getTypeField(ArchiveFile extension) {
    var dcType = extension.getField("dc:type");
    if (dcType != null) {
      return dcType;
    } else {
      return extension.getField("dcterms:type");
    }
  }

  private void processDigitalSpecimen(ArchiveFile core) throws IOException {
    var hasAssociatedMedia = core.hasTerm(DWC_ASSOCIATED_MEDIA);
    for (var rec : core) {
      if (recordNeedsToBeIgnored(rec, core)) {
        idTobeIgnored.add(rec.id());
      } else {
        var organizationId = getProperty("organization_id", core, rec);
        var physicalSpecimenIdType = getProperty("physical_specimen_id_type", core, rec);
        var digitalSpecimen = new DigitalSpecimen(getProperty("type", core, rec),
            getPhysicalSpecimenId(physicalSpecimenIdType, organizationId, core, rec),
            physicalSpecimenIdType, getProperty("physical_specimen_collection", core, rec),
            getProperty("specimen_name", core, rec), organizationId,
            getProperty("dataset_id", core, rec), webClientProperties.getSourceSystemId(),
            getData(core, rec, false), getData(core, rec, true),
            rec.id());
        log.info("Digital Specimen: {}", digitalSpecimen);
        var translatorEvent = new DigitalSpecimenEvent(enrichmentServices(false), digitalSpecimen);
        kafkaService.sendMessage("digital-specimen", mapper.writeValueAsString(translatorEvent));
        if (hasAssociatedMedia) {
          var associatedMedia = rec.value(core.getField(DWC_ASSOCIATED_MEDIA).getTerm());
          if (associatedMedia != null) {
            publishAssociatedMedia(associatedMedia, digitalSpecimen);
          }
        }
      }
    }
  }

  private boolean recordNeedsToBeIgnored(Record rec, ArchiveFile core) {
    if (!core.hasTerm("dwc:basisOfRecord")) {
      log.warn("Record with id: {} is missing the basis of Record, Record will be ignored",
          rec.id());
      return true;
    } else {
      var basisOfRecord = rec.value(core.getField("dwc:basisOfRecord").getTerm());
      if (basisOfRecord == null) {
        log.warn("Record with id: {} has an empty the basis of Record, Record will be ignored",
            rec.id());
        return true;
      }
      basisOfRecord = basisOfRecord.toUpperCase(Locale.ROOT);
      if (allowedBasisOfRecord.contains(basisOfRecord)) {
        return false;
      } else {
        log.warn("Record with id: {} has basisOfRecord: {} which is not a physical specimen",
            rec.id(), basisOfRecord);
        return true;
      }
    }
  }

  private void publishAssociatedMedia(String associatedMedia, DigitalSpecimen digitalSpecimen)
      throws JsonProcessingException {
    log.info("Digital Specimen: {}, has associatedMedia {}", digitalSpecimen.physicalSpecimenId(),
        associatedMedia);
    String[] mediaUrls = associatedMedia.split("\\|");
    for (var mediaUrl : mediaUrls) {
      var digitalMediaObject = new DigitalMediaObject(UNKNOWN,
          digitalSpecimen.physicalSpecimenId(), mediaUrl, null,
          webClientProperties.getSourceSystemId(), null, null, PHYSICAL_SPECIMEN_ID);
      publishDigitalMediaObject(digitalMediaObject);
    }
  }

  private String getPhysicalSpecimenId(String physicalSpecimenIdType, String organizationId,
      ArchiveFile core, Record rec) {
    if (physicalSpecimenIdType.equals("cetaf")) {
      return getProperty(PHYSICAL_SPECIMEN_ID, core, rec);
    } else if (physicalSpecimenIdType.equals("combined")) {
      return getProperty(PHYSICAL_SPECIMEN_ID, core, rec) + ":" + minifyOrganizationId(
          organizationId);
    } else {
      log.warn("Unknown physicalSpecimenIdType specified");
      return UNKNOWN;
    }
  }

  private String minifyOrganizationId(String organizationId) {
    if (organizationId.startsWith("https://ror.org")) {
      return organizationId.replace("https://ror.org/", "");
    } else {
      log.warn("Cannot determine organizationId: {} for combined physicalSpecimenId",
          organizationId);
      return "UnknownOrganisationUrl";
    }
  }

  private String getProperty(String fieldName, ArchiveFile core, Record rec) {
    if (mapping.getDefaultMappings().containsKey(fieldName)) {
      return mapping.getDefaultMappings().get(fieldName);
    } else if (mapping.getFieldMappings().containsKey(fieldName)) {
      return rec.value(core.getField(mapping.getFieldMappings().get(fieldName)).getTerm());
    } else {
      log.warn("Cannot find property {}", fieldName);
      return UNKNOWN;
    }
  }

  private JsonNode getData(ArchiveFile core, Record rec, boolean includeMappedFields) {
    var data = mapper.createObjectNode();
    for (var field : core.getFields().keySet()) {
      if (rec.value(field) != null) {
        if (includeMappedFields) {
          data.put(field.prefixedName(), rec.value(field));
        } else {
          if (!mapping.getFieldMappings().containsValue(field.prefixedName())
              && !DEFAULT_MAPPED_FIELD.contains(field.prefixedName())) {
            data.put(field.prefixedName(), rec.value(field));
          }
        }
      }
    }
    return data;
  }

}
