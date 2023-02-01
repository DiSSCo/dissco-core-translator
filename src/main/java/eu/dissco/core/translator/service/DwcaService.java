package eu.dissco.core.translator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.Profiles;
import eu.dissco.core.translator.domain.DigitalMediaObject;
import eu.dissco.core.translator.domain.DigitalMediaObjectEvent;
import eu.dissco.core.translator.domain.DigitalSpecimen;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.domain.Enrichment;
import eu.dissco.core.translator.properties.DwcaProperties;
import eu.dissco.core.translator.properties.EnrichmentProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.SourceSystemRepository;
import eu.dissco.core.translator.terms.media.AccessUri;
import eu.dissco.core.translator.terms.specimen.DatasetId;
import eu.dissco.core.translator.terms.specimen.DwcaId;
import eu.dissco.core.translator.terms.media.Format;
import eu.dissco.core.translator.terms.License;
import eu.dissco.core.translator.terms.media.MediaType;
import eu.dissco.core.translator.terms.specimen.Modified;
import eu.dissco.core.translator.terms.specimen.ObjectType;
import eu.dissco.core.translator.terms.specimen.OrganisationId;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenCollection;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenId;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenIdType;
import eu.dissco.core.translator.terms.SourceSystemId;
import eu.dissco.core.translator.terms.specimen.SpecimenName;
import eu.dissco.core.translator.terms.TermMapper;
import eu.dissco.core.translator.terms.specimen.Type;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gbif.dwc.Archive;
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


  private static final String DWC_ASSOCIATED_MEDIA = "dwc:associatedMedia";
  private static final String UNKNOWN = "Unknown";
  private static final String PHYSICAL_SPECIMEN_ID = "physical_specimen_id";

  private final ObjectMapper mapper;
  private final WebClientProperties webClientProperties;
  private final WebClient webClient;
  private final DwcaProperties dwcaProperties;
  private final KafkaService kafkaService;
  private final TermMapper termMapper;
  private final EnrichmentProperties enrichmentProperties;
  private final SourceSystemRepository repository;
  private final Set<String> idTobeIgnored = new HashSet<>();
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
      if (idTobeIgnored.contains(rec.id())) {
        log.info("Skipped media with id: {} as the specimen has been ignored", rec.id());
      } else {
        var type = termMapper.retrieveFromDWCA(new MediaType(), extension, rec);
        log.info("Type of digitalMediaObject is: {}", type);
        var digitalMediaObject = new DigitalMediaObject(
            type,
            rec.id(),
            "DWCA",
            harmonizeMedia(extension, rec),
            getOriginalAttributes(extension, rec));
        publishDigitalMediaObject(digitalMediaObject);
      }
    }
  }

  private void extractACMultimedia(ArchiveFile extension) throws JsonProcessingException {
    for (var rec : extension) {
      if (idTobeIgnored.contains(rec.id())) {
        log.info("Skipped media with id: {} as the specimen has been ignored", rec.id());
      } else {
        var type = termMapper.retrieveFromDWCA(new MediaType(), extension, rec);
        log.info("Type of digitalMediaObject is: {}", type);
        var digitalMediaObject = new DigitalMediaObject(
            type,
            rec.id(),
            "DWCA",
            harmonizeMedia(extension, rec),
            getOriginalAttributes(extension, rec)
        );
        publishDigitalMediaObject(digitalMediaObject);
      }
    }
  }

  private JsonNode harmonizeMedia(ArchiveFile extension, Record rec) {
    var attributes = mapper.createObjectNode();
    attributes.put(AccessUri.TERM, termMapper.retrieveFromDWCA(new AccessUri(), extension, rec));
    attributes.put(SourceSystemId.TERM, webClientProperties.getSourceSystemId());
    attributes.put(Format.TERM, termMapper.retrieveFromDWCA(new Format(), extension, rec));
    attributes.put(License.TERM, termMapper.retrieveFromDWCA(new License(), extension, rec));
    return attributes;
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

  private void processDigitalSpecimen(ArchiveFile core) throws IOException {
    var hasAssociatedMedia = core.hasTerm(DWC_ASSOCIATED_MEDIA);
    for (var rec : core) {
      if (recordNeedsToBeIgnored(rec, core)) {
        idTobeIgnored.add(rec.id());
      } else {
        var digitalSpecimen = createDigitalSpecimen(core, rec);
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

  private DigitalSpecimen createDigitalSpecimen(ArchiveFile core, Record rec) {
    var physicalSpecimenIdType = termMapper.retrieveFromDWCA(new PhysicalSpecimenIdType(), core,
        rec);
    var organizationId = termMapper.retrieveFromDWCA(new OrganisationId(), core, rec);
    return new DigitalSpecimen(
        getPhysicalSpecimenId(physicalSpecimenIdType, organizationId, core, rec),
        termMapper.retrieveFromDWCA(new Type(), core, rec),
        harmonizeSpecimenAttributes(physicalSpecimenIdType, organizationId, core, rec),
        getOriginalAttributes(core, rec)
    );
  }

  private JsonNode harmonizeSpecimenAttributes(String physicalSpecimenIdType, String organizationId,
      ArchiveFile core, Record rec) {
    var attributes = mapper.createObjectNode();
    attributes.put(License.TERM, termMapper.retrieveFromDWCA(new License(), core, rec));
    attributes.put(PhysicalSpecimenIdType.TERM, physicalSpecimenIdType);
    attributes.put(OrganisationId.TERM, organizationId);
    attributes.put(SpecimenName.TERM, termMapper.retrieveFromDWCA(new SpecimenName(), core, rec));
    attributes.put(PhysicalSpecimenCollection.TERM,
        termMapper.retrieveFromDWCA(new PhysicalSpecimenCollection(), core, rec));
    attributes.put(DatasetId.TERM, termMapper.retrieveFromDWCA(new SpecimenName(), core, rec));
    attributes.put(SourceSystemId.TERM, webClientProperties.getSourceSystemId());
    attributes.put(ObjectType.TERM, termMapper.retrieveFromDWCA(new ObjectType(), core, rec));
    attributes.put(Modified.TERM, termMapper.retrieveFromDWCA(new Modified(), core, rec));
    attributes.put(DwcaId.TERM, rec.id());
    return attributes;
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
    log.info("Digital Specimen: {}, has associatedMedia {}", digitalSpecimen.id(),
        associatedMedia);
    String[] mediaUrls = associatedMedia.split("\\|");
    for (var mediaUrl : mediaUrls) {
      var digitalMediaObject = new DigitalMediaObject(
          UNKNOWN,
          digitalSpecimen.id(),
          PHYSICAL_SPECIMEN_ID,
          harmonizeAssociatedMedia(mediaUrl),
          null);
      publishDigitalMediaObject(digitalMediaObject);
    }
  }

  private JsonNode harmonizeAssociatedMedia(String mediaUrl) {
    var attributes = mapper.createObjectNode();
    attributes.put(AccessUri.TERM, mediaUrl);
    attributes.put(SourceSystemId.TERM, webClientProperties.getSourceSystemId());
    return attributes;
  }

  private String getPhysicalSpecimenId(String physicalSpecimenIdType, String organizationId,
      ArchiveFile core, Record rec) {
    String id = termMapper.retrieveFromDWCA(new PhysicalSpecimenId(), core, rec);
    if (physicalSpecimenIdType.equals("cetaf")) {
      return id;
    } else if (physicalSpecimenIdType.equals("combined")) {
      return id + ":" + minifyOrganizationId(organizationId);
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

  private JsonNode getOriginalAttributes(ArchiveFile core, Record rec) {
    var data = mapper.createObjectNode();
    for (var field : core.getFields().keySet()) {
      if (rec.value(field) != null) {
        data.put(field.prefixedName(), rec.value(field));
      }
    }
    return data;
  }

}
