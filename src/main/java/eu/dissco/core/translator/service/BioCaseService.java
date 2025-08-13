package eu.dissco.core.translator.service;


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import efg.DataSets;
import efg.DataSets.DataSet;
import efg.EarthScienceSpecimenType;
import efg.MineralRockIdentifiedType;
import efg.MultiMediaObject;
import efg.Unit;
import eu.dissco.core.translator.Profiles;
import eu.dissco.core.translator.component.EmlComponent;
import eu.dissco.core.translator.component.SourceSystemComponent;
import eu.dissco.core.translator.database.jooq.enums.JobState;
import eu.dissco.core.translator.domain.BioCasePartResult;
import eu.dissco.core.translator.domain.DigitalMediaEvent;
import eu.dissco.core.translator.domain.DigitalMediaWrapper;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.domain.DigitalSpecimenWrapper;
import eu.dissco.core.translator.domain.TranslatorJobResult;
import eu.dissco.core.translator.exception.DiSSCoDataException;
import eu.dissco.core.translator.exception.DisscoEfgParsingException;
import eu.dissco.core.translator.exception.ReachedMaximumLimitException;
import eu.dissco.core.translator.properties.ApplicationProperties;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.properties.MasProperties;
import eu.dissco.core.translator.terms.BaseDigitalObjectDirector;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Node;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

@Slf4j
@Service
@Profile(Profiles.BIOCASE)
@RequiredArgsConstructor
public class BioCaseService extends WebClientService {

  private static final String START_AT = "startAt";
  private static final String LIMIT = "limit";
  private static final String FILTERS = "filters";
  private static final String ABCD = "abcd:";
  private static final String ABCDEFG = "abcd-efg:";

  private static final List<String> allowedBasisOfRecord = List.of("PRESERVEDSPECIMEN", "FOSSIL",
      "OTHER", "ROCK", "MINERAL", "METEORITE", "FOSSILSPECIMEN", "LIVINGSPECIMEN", "MATERIALSAMPLE",
      "FOSSIL SPECIMEN", "ROCKSPECIMEN", "ROCK SPECIMEN", "MINERALSPECIMEN", "MINERAL SPECIMEN",
      "METEORITESPECIMEN", "METEORITE SPECIMEN", "HERBARIUM SHEET", "HERBARIUMSHEET", "DRIED");

  private final ObjectMapper mapper;
  private final ApplicationProperties applicationProperties;
  private final WebClient webClient;
  private final SourceSystemComponent sourceSystemComponent;
  private final Configuration configuration;
  private final XMLInputFactory xmlFactory;
  private final RabbitMqService rabbitMqService;
  private final MasProperties masProperties;
  private final BaseDigitalObjectDirector digitalSpecimenDirector;
  private final FdoProperties fdoProperties;

  private boolean isAcceptedBasisOfRecord(Unit unit) {
    var recordBasis = unit.getRecordBasis();
    if (recordBasis != null && !recordBasis.isBlank()) {
      return allowedBasisOfRecord.contains(recordBasis.strip().toUpperCase());
    }
    return false;
  }

  @Override
  public TranslatorJobResult retrieveData() {
    var uri = sourceSystemComponent.getSourceSystemEndpoint();
    var templateProperties = getTemplateProperties();
    configuration.setNumberFormat("computer");
    var finished = false;
    var processedRecords = new AtomicInteger(0);
    while (!finished) {
      log.info("Currently at: {} still collecting...", templateProperties.get(START_AT));
      StringWriter writer = fillTemplate(templateProperties);
      try {
        var partResult = webClient.get().uri(uri + writer)
            .retrieve()
            .bodyToMono(String.class).publishOn(Schedulers.boundedElastic()).map(
                (String xml) -> mapToABCD(xml, processedRecords))
            .retryWhen(Retry.backoff(3, Duration.ofSeconds(2)))
            .toFuture().get();
        if (partResult.exception()) {
          return new TranslatorJobResult(JobState.FAILED, processedRecords.get());
        }
        finished = partResult.finished();
        if (finished) {
          log.info("Unable to get records from xml");
        }
      } catch (InterruptedException e) {
        log.error("Failed to get response from uri", e);
        Thread.currentThread().interrupt();
        return new TranslatorJobResult(JobState.FAILED, processedRecords.get());
      } catch (ExecutionException e) {
        log.error("Failed to get response from uri", e);
        return new TranslatorJobResult(JobState.FAILED, processedRecords.get());
      }
      updateStartAtParameter(templateProperties);
    }
    if (processedRecords.get() == 0) {
      log.info("No records were successfully processed");
      return new TranslatorJobResult(JobState.FAILED, processedRecords.get());
    }
    return new TranslatorJobResult(JobState.COMPLETED, processedRecords.get());
  }

  private BioCasePartResult mapToABCD(String xml, AtomicInteger processedRecords) {
    var recordCount = 0;
    var recordDropped = 0;
    try {
      var xmlEventReader = xmlFactory.createXMLEventReader(new StringReader(xml));
      while (xmlEventReader.hasNext()) {
        var element = xmlEventReader.nextEvent();
        if (isStartElement(element, "content")) {
          recordCount = Integer.parseInt(
              element.asStartElement().getAttributeByName(new QName("recordCount")).getValue());
          recordDropped = Integer.parseInt(
              element.asStartElement().getAttributeByName(new QName("recordDropped")).getValue());
          log.info("Received {} records in BioCase request, {} records dropped", recordCount,
              recordDropped);
        }
        retrieveUnitData(xmlEventReader, processedRecords);
      }
      if ((recordCount + recordDropped) % applicationProperties.getItemsPerRequest() != 0) {
        log.info("Received records {} does not match requested records {}. "
                + "All records have been processed", (recordCount + recordDropped),
            applicationProperties.getItemsPerRequest());
        return new BioCasePartResult(true, false);
      } else {
        return new BioCasePartResult(false, false);
      }
    } catch (XMLStreamException | JAXBException | IOException e) {
      log.error("Error converting response to XML", e);
      return new BioCasePartResult(true, true);
    } catch (ReachedMaximumLimitException e) {
      log.warn("Reached maximum limit of {} processed specimens",
          applicationProperties.getMaxItems());
      return new BioCasePartResult(true, false);
    }
  }

  private void retrieveUnitData(XMLEventReader xmlEventReader, AtomicInteger processedRecords)
      throws XMLStreamException, JAXBException, ReachedMaximumLimitException, IOException {
    mapper.setSerializationInclusion(Include.NON_NULL);
    if (isStartElement(xmlEventReader.peek(), "DataSets")) {
      mapABCD206(xmlEventReader, processedRecords);
    }
  }

  private void mapABCD206(XMLEventReader xmlEventReader, AtomicInteger processedRecords)
      throws JAXBException, ReachedMaximumLimitException, IOException {
    var context = JAXBContext.newInstance(DataSets.class);
    var datasetsMarshaller = context.createUnmarshaller().unmarshal(xmlEventReader, DataSets.class);
    var datasets = datasetsMarshaller.getValue().getDataSet().get(0);
    sourceSystemComponent.storeEmlRecord(EmlComponent.generateEML(datasets));
    for (var unit : datasets.getUnits().getUnit()) {
      try {
        if (applicationProperties.getMaxItems() != null
            && processedRecords.get() >= applicationProperties.getMaxItems()) {
          throw new ReachedMaximumLimitException();
        }
        processUnit(datasets, unit, processedRecords);
      } catch (DisscoEfgParsingException | JsonProcessingException e) {
        log.error("Unit could not be processed due to error", e);
      }
    }
  }

  private void processUnit(DataSet dataset, Unit unit, AtomicInteger processedRecords)
      throws JsonProcessingException, DisscoEfgParsingException {
    var unitAttributes = parseToJson(unit);
    var datasetAttribute = getData(mapper.valueToTree(dataset.getMetadata()));
    unitAttributes.setAll(datasetAttribute);
    unitAttributes.put("abcd:datasetGUID", dataset.getDatasetGUID());
    if (isAcceptedBasisOfRecord(unit)) {
      try {
        var attributes = digitalSpecimenDirector.assembleDigitalSpecimenTerm(unitAttributes, false);
        if (attributes.getOdsNormalisedPhysicalSpecimenID() == null
            || attributes.getOdsOrganisationID() == null) {
          throw new DiSSCoDataException(
              "Record does not comply to MIDS level 0 (id and organisation), ignoring record");
        }
        var digitalSpecimen = new DigitalSpecimenWrapper(
            attributes.getOdsNormalisedPhysicalSpecimenID(),
            fdoProperties.getDigitalSpecimenType(),
            attributes,
            cleanupRedundantFields(unitAttributes)
        );
        var digitalMedia = makeDigitalMediaUnique(processDigitalMedia(
            attributes.getOdsNormalisedPhysicalSpecimenID(), unit,
            attributes.getOdsOrganisationID()));
        log.debug("Result digital Specimen: {}", digitalSpecimen);
        rabbitMqService.sendMessage(
            new DigitalSpecimenEvent(
                machineAnnotationServices(false),
                digitalSpecimen,
                digitalMedia, masProperties.getForceMasSchedule()));
        processedRecords.incrementAndGet();
      } catch (DiSSCoDataException e) {
        log.error("Encountered data issue with record: {}", unitAttributes, e);
      }
    } else {
      log.info("Record with record basis: {} and id: {} is ignored ", unit.getRecordBasis(),
          unit.getUnitID());
    }
  }

  private List<DigitalMediaEvent> makeDigitalMediaUnique(List<DigitalMediaEvent> digitalMedia) {
    return digitalMedia.stream()
        .collect(Collectors.toMap(
            mediaEvent -> mediaEvent.digitalMedia().attributes().getAcAccessURI(),
            mediaEvent -> mediaEvent,
            (existing, replacement) -> replacement,
            LinkedHashMap::new))
        .values()
        .stream()
        .toList();
  }

  private ObjectNode parseToJson(Unit unit) throws DisscoEfgParsingException {
    var unitData = getData(mapper.valueToTree(unit));
    extractEfgInformation(unit, unitData);
    return unitData;
  }

  private void extractEfgInformation(Unit unit, ObjectNode unitAttributes)
      throws DisscoEfgParsingException {
    var efgTaxExtension = getEfgTaxExtension(unit);
    if (!efgTaxExtension.isEmpty()) {
      addEfgFieldsForIdentifications(unitAttributes, efgTaxExtension);
    }
    var efgUnitExtension = getEfgUnitExtension(unit);
    if (efgUnitExtension != null) {
      addEfgFieldsForExtension(unitAttributes, efgUnitExtension);
    }
  }

  private void addEfgFieldsForIdentifications(ObjectNode unitAttributes,
      List<MineralRockIdentifiedType> efgTaxExtension) {
    for (int i = 0; i < efgTaxExtension.size(); i++) {
      StringBuilder prefix = new StringBuilder(
          "identifications/identification/" + i + "/result/mineralRockIdentified/");
      MineralRockIdentifiedType identification = efgTaxExtension.get(i);
      iterateOverNode(mapper.valueToTree(identification), unitAttributes, ABCDEFG, prefix);
      unitAttributes.remove("abcd:identifications/identification/" + i + "/result/extension");
    }
  }

  private List<MineralRockIdentifiedType> getEfgTaxExtension(Unit unit)
      throws DisscoEfgParsingException {
    var efgIdentifications = new ArrayList<MineralRockIdentifiedType>();
    if (unit.getIdentifications() != null && !unit.getIdentifications().getIdentification()
        .isEmpty()) {
      for (var identification : unit.getIdentifications().getIdentification()) {
        var node = (Node) identification.getResult().getExtension();
        if (node != null) {
          var document = node.getOwnerDocument();
          Source xmlSource = new DOMSource(document);
          try {
            var context = JAXBContext.newInstance(MineralRockIdentifiedType.class);
            extractMineralRockIdentification(efgIdentifications, context, xmlSource);
          } catch (XMLStreamException | JAXBException e) {
            throw new DisscoEfgParsingException(
                "Failed to parse XML extension for EarthScienceSpecimen XML", e);
          }
        }
      }
    }
    return efgIdentifications;
  }

  private void extractMineralRockIdentification(
      ArrayList<MineralRockIdentifiedType> efgIdentifications,
      JAXBContext context, Source xmlSource) throws XMLStreamException, DisscoEfgParsingException {
    var xmlEventReader = xmlFactory.createXMLEventReader(xmlSource);
    while (xmlEventReader.hasNext()) {
      xmlEventReader.nextEvent();
      if (isStartElement(xmlEventReader.peek(), "MineralRockIdentified")) {
        try {
          efgIdentifications.add(context.createUnmarshaller()
              .unmarshal(xmlEventReader, MineralRockIdentifiedType.class)
              .getValue());
        } catch (JAXBException e) {
          throw new DisscoEfgParsingException("Failed to map to MineralRockIdentified object",
              e);
        }
      }
    }
  }

  private EarthScienceSpecimenType getEfgUnitExtension(Unit unit) throws DisscoEfgParsingException {
    var node = (Node) unit.getUnitExtension();
    if (node != null) {
      var document = node.getOwnerDocument();
      Source xmlSource = new DOMSource(document);
      try {
        var context = JAXBContext.newInstance(EarthScienceSpecimenType.class);
        return extractEarthScienceSpecimenType(xmlSource, context);
      } catch (JAXBException | XMLStreamException e) {
        throw new DisscoEfgParsingException(
            "Failed to parse XML extension for EarthScienceSpecimen XML", e);
      }
    }
    return null;
  }

  private EarthScienceSpecimenType extractEarthScienceSpecimenType(Source xmlSource,
      JAXBContext context)
      throws DisscoEfgParsingException, XMLStreamException {
    var xmlEventReader = xmlFactory.createXMLEventReader(xmlSource);
    while (xmlEventReader.hasNext()) {
      xmlEventReader.nextEvent();
      if (isStartElement(xmlEventReader.peek(), "EarthScienceSpecimen")) {
        try {
          return context.createUnmarshaller()
              .unmarshal(xmlEventReader, EarthScienceSpecimenType.class)
              .getValue();
        } catch (JAXBException e) {
          throw new DisscoEfgParsingException("Failed to map to EarthScienceSpecimen object",
              e);
        }
      }
    }
    throw new DisscoEfgParsingException("Unable to find EarthScienceSpecimen in extension");
  }

  private void addEfgFieldsForExtension(ObjectNode unitData, EarthScienceSpecimenType efg) {
    StringBuilder prefix = new StringBuilder("earthScienceSpecimen/");
    iterateOverNode(mapper.valueToTree(efg), unitData, ABCDEFG, prefix);
    unitData.remove("abcd:unitExtension");
  }

  private JsonNode cleanupRedundantFields(JsonNode unitData) {
    var multiMediaFields = new ArrayList<String>();
    var data = (ObjectNode) unitData.deepCopy();
    data.fields().forEachRemaining(field -> {
      if (field.getKey().startsWith("abcd:multiMediaObjects")) {
        multiMediaFields.add(field.getKey());
      }
    });
    data.remove(multiMediaFields);
    return data;
  }

  private List<DigitalMediaEvent> processDigitalMedia(String physicalSpecimenId,
      Unit unit, String organisationId) {
    var digitalMediaEvents = new ArrayList<DigitalMediaEvent>();
    if (unit.getMultiMediaObjects() != null && !unit.getMultiMediaObjects().getMultiMediaObject()
        .isEmpty()) {
      for (MultiMediaObject media : unit.getMultiMediaObjects().getMultiMediaObject()) {
        try {
          digitalMediaEvents.add(
              processDigitalMedia(media, organisationId));
        } catch (DiSSCoDataException e) {
          log.error("Failed to process digital media object for digital specimen: {}",
              physicalSpecimenId, e);
        }
      }
    }
    return digitalMediaEvents;
  }

  private DigitalMediaEvent processDigitalMedia(MultiMediaObject media, String organisationId)
      throws DiSSCoDataException {
    var attributes = getData(mapper.valueToTree(media));
    var digitalMedia = digitalSpecimenDirector.assembleDigitalMedia(false, attributes,
        organisationId);
    if (digitalMedia.getAcAccessURI() == null) {
      throw new DiSSCoDataException(
          "Digital media object for specimen does not have an access uri, ignoring record");
    }
    var digitalMediaEvent = new DigitalMediaEvent(machineAnnotationServices(true),
        new DigitalMediaWrapper(
            fdoProperties.getDigitalMediaType(),
            digitalMedia,
            attributes
        ), masProperties.getForceMasSchedule());
    log.debug("Result digital media object: {}", digitalMediaEvent);
    return digitalMediaEvent;
  }

  private Set<String> machineAnnotationServices(boolean mediaObject) {
    if (mediaObject) {
      return Stream.concat(masProperties.getAdditionalMediaMass().stream(),
          sourceSystemComponent.getMediaMass().stream()).collect(
          Collectors.toSet());
    } else {
      return Stream.concat(masProperties.getAdditionalSpecimenMass().stream(),
          sourceSystemComponent.getSpecimenMass().stream()).collect(
          Collectors.toSet());
    }
  }

  private StringWriter fillTemplate(Map<String, Object> templateProperties) {
    var writer = new StringWriter();
    try {
      var template = configuration.getTemplate("biocase-request.ftl");
      template.process(templateProperties, writer);
    } catch (IOException | TemplateException e) {
      log.error("Failed to retrieve template", e);
    }
    return writer;
  }

  private Map<String, Object> getTemplateProperties() {
    var map = new HashMap<String, Object>();
    map.put(LIMIT, applicationProperties.getItemsPerRequest());
    map.put(START_AT, 0);
    map.put(FILTERS, sourceSystemComponent.getSourceSystemFilters());
    return map;
  }

  private void updateStartAtParameter(Map<String, Object> templateProperties) {
    templateProperties.put(START_AT,
        ((int) templateProperties.get(START_AT) + (int) templateProperties.get(LIMIT)));
  }

  private ObjectNode getData(JsonNode node) {
    var data = mapper.createObjectNode();
    StringBuilder prefix = new StringBuilder();
    iterateOverNode(node, data, ABCD, prefix);
    return data;
  }

  private void iterateOverNode(JsonNode node, ObjectNode data, String nameSpace,
      StringBuilder prefix) {
    node.fields().forEachRemaining(
        field -> {
          if (field.getValue().isObject()) {
            walkThroughObjects(data, prefix, field, nameSpace);
          } else if (field.getValue().isArray()) {
            walkThroughArray(data, prefix, field, nameSpace);
          } else {
            getValueFromField(data, prefix, field, nameSpace);
          }
        }
    );
  }

  private void getValueFromField(ObjectNode data, StringBuilder prefix,
      Entry<String, JsonNode> field, String nameSpace) {
    if (field.getValue().isTextual()) {
      data.put(nameSpace + prefix + field.getKey(), field.getValue().textValue());
    } else if (field.getValue().isDouble()) {
      data.put(nameSpace + prefix + field.getKey(), field.getValue().doubleValue());
    } else if (field.getValue().isInt()) {
      data.put(nameSpace + prefix + field.getKey(), field.getValue().intValue());
    } else if (field.getValue().isBoolean()) {
      data.put(nameSpace + prefix + field.getKey(), field.getValue().booleanValue());
    } else if (field.getValue().isBigDecimal()) {
      data.put(nameSpace + prefix + field.getKey(), field.getValue().decimalValue());
    } else if (field.getValue().isBigInteger()) {
      data.put(nameSpace + prefix + field.getKey(), field.getValue().bigIntegerValue());
    } else if (field.getValue().isLong()) {
      data.put(nameSpace + prefix + field.getKey(), field.getValue().asLong());
    } else {
      log.warn("Field could not be mapped: {}", field.getKey());
    }
  }

  private void walkThroughObjects(ObjectNode data, StringBuilder prefix,
      Entry<String, JsonNode> field, String namespace) {
    prefix.append(field.getKey()).append("/");
    iterateOverNode(field.getValue(), data, namespace, prefix);
    prefix.delete(prefix.length() - field.getKey().length() - 1, prefix.length());
  }

  private void walkThroughArray(ObjectNode data, StringBuilder prefix,
      Entry<String, JsonNode> field, String namespace) {
    var count = 0;
    for (JsonNode item : field.getValue()) {
      prefix.append(field.getKey()).append("/").append(count).append("/");
      iterateOverNode(item, data, namespace, prefix);
      prefix.delete(prefix.length() - field.getKey().length() - 3, prefix.length());
      count = count + 1;
    }
  }

}
