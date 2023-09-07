package eu.dissco.core.translator.service;


import static eu.dissco.core.translator.service.IngestionUtility.convertToPhysicalSpecimenIdTypeEnum;
import static eu.dissco.core.translator.service.IngestionUtility.getPhysicalSpecimenId;

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
import eu.dissco.core.translator.component.RorComponent;
import eu.dissco.core.translator.domain.DigitalMediaObject;
import eu.dissco.core.translator.domain.DigitalMediaObjectEvent;
import eu.dissco.core.translator.domain.DigitalSpecimen;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.domain.Enrichment;
import eu.dissco.core.translator.exception.DiSSCoDataException;
import eu.dissco.core.translator.exception.DisscoEfgParsingException;
import eu.dissco.core.translator.properties.EnrichmentProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.SourceSystemRepository;
import eu.dissco.core.translator.terms.DigitalSpecimenDirector;
import eu.dissco.core.translator.terms.License;
import eu.dissco.core.translator.terms.SourceSystemId;
import eu.dissco.core.translator.terms.TermMapper;
import eu.dissco.core.translator.terms.media.AccessUri;
import eu.dissco.core.translator.terms.media.Format;
import eu.dissco.core.translator.terms.media.MediaType;
import eu.dissco.core.translator.terms.specimen.OrganisationId;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenId;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenIdType;
import eu.dissco.core.translator.terms.specimen.Type;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.w3c.dom.Node;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
@Profile(Profiles.BIOCASE)
@RequiredArgsConstructor
public class BioCaseService implements WebClientService {

  private static final String START_AT = "startAt";
  private static final String LIMIT = "limit";
  private static final String ABCD = "abcd:";
  private static final String ABCDEFG = "abcd-efg:";

  private static final List<String> allowedBasisOfRecord = List.of("PRESERVEDSPECIMEN", "FOSSIL",
      "OTHER", "ROCK", "MINERAL", "METEORITE", "FOSSILSPECIMEN", "LIVINGSPECIMEN", "MATERIALSAMPLE",
      "FOSSIL SPECIMEN", "ROCKSPECIMEN", "ROCK SPECIMEN", "MINERALSPECIMEN", "MINERAL SPECIMEN",
      "METEORITESPECIMEN", "METEORITE SPECIMEN");

  private final ObjectMapper mapper;
  private final WebClientProperties webClientProperties;
  private final WebClient webClient;
  private final SourceSystemRepository repository;
  private final Configuration configuration;
  private final XMLInputFactory xmlFactory;
  private final TermMapper termMapper;
  private final KafkaService kafkaService;
  private final EnrichmentProperties enrichmentProperties;
  private final RorComponent rorComponent;
  private final DigitalSpecimenDirector digitalSpecimenDirector;

  private boolean isAcceptedBasisOfRecord(Unit unit) {
    var recordBasis = unit.getRecordBasis();
    if (recordBasis != null && !recordBasis.isBlank()) {
      return allowedBasisOfRecord.contains(recordBasis.strip().toUpperCase());
    }
    return false;
  }

  @Override
  public void retrieveData() {
    var uri = repository.getEndpoint(webClientProperties.getSourceSystemId());
    var templateProperties = getTemplateProperties();
    configuration.setNumberFormat("computer");
    var finished = false;
    while (!finished) {
      log.info("Currently at: {} still collecting...", templateProperties.get(START_AT));
      StringWriter writer = fillTemplate(templateProperties);
      try {
        finished = webClient.get().uri(uri + writer)
            .retrieve()
            .bodyToMono(String.class).publishOn(Schedulers.boundedElastic()).map(this::mapToABCD)
            .toFuture().get();
        if (finished) {
          log.info("Unable to get records from xml");
        }
      } catch (InterruptedException | ExecutionException e) {
        log.error("Failed to get response from uri", e);
        Thread.currentThread().interrupt();
        return;
      }
      updateStartAtParameter(templateProperties);
    }
  }

  private boolean mapToABCD(String xml) {
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
        retrieveUnitData(xmlEventReader);
      }
      if ((recordCount + recordDropped) % webClientProperties.getItemsPerRequest() != 0) {
        log.info("Received records {} does not match requested records {}. "
                + "All records have been processed", (recordCount + recordDropped),
            webClientProperties.getItemsPerRequest());
        return true;
      } else {
        return false;
      }
    } catch (XMLStreamException | JAXBException e) {
      log.error("Error converting response tot XML", e);
    }
    return false;
  }

  private void retrieveUnitData(XMLEventReader xmlEventReader)
      throws XMLStreamException, JAXBException {
    mapper.setSerializationInclusion(Include.NON_NULL);
    if (isStartElement(xmlEventReader.peek(), "DataSets")) {
      mapABCD206(xmlEventReader);
    }
  }

  private void mapABCD206(XMLEventReader xmlEventReader)
      throws JAXBException {
    var context = JAXBContext.newInstance(DataSets.class);
    var datasetsMarshaller = context.createUnmarshaller().unmarshal(xmlEventReader, DataSets.class);
    var datasets = datasetsMarshaller.getValue().getDataSet().get(0);
    for (var unit : datasets.getUnits().getUnit()) {
      try {
        processUnit(datasets, unit);
      } catch (DisscoEfgParsingException | JsonProcessingException e) {
        log.error("Unit could not be processed due to error", e);
      }
    }
  }

  private void processUnit(DataSet dataset, Unit unit)
      throws JsonProcessingException, DisscoEfgParsingException {
    var unitAttributes = parseToJson(unit);
    var datasetAttribute = getData(mapper.valueToTree(dataset.getMetadata()));
    unitAttributes.setAll(datasetAttribute);
    if (isAcceptedBasisOfRecord(unit)) {
      var physicalSpecimenIdType = termMapper.retrieveTerm(new PhysicalSpecimenIdType(),
          unitAttributes, true);
      var organisationId = termMapper.retrieveTerm(new OrganisationId(), unitAttributes, false);
      if (physicalSpecimenIdType != null) {
        try {
          var physicalSpecimenId = getPhysicalSpecimenId(physicalSpecimenIdType, organisationId,
              termMapper.retrieveTerm(new PhysicalSpecimenId(), unitAttributes, false));
          var ds = new eu.dissco.core.translator.schema.DigitalSpecimen()
              .withOdsPhysicalSpecimenIdType(convertToPhysicalSpecimenIdTypeEnum(physicalSpecimenIdType))
              .withDwcInstitutionId(organisationId)
              .withOdsPhysicalSpecimenId(physicalSpecimenId)
              .withOdsSourceSystem(webClientProperties.getSourceSystemId());
          var digitalSpecimen =  new DigitalSpecimen(
              physicalSpecimenId,
              termMapper.retrieveTerm(new Type(), unitAttributes, false),
              digitalSpecimenDirector.constructDigitalSpecimen(ds, false, unitAttributes),
              cleanupRedundantFields(unitAttributes)
          );
          log.debug("Result digital Specimen: {}", digitalSpecimen);
          kafkaService.sendMessage("digital-specimen",
              mapper.writeValueAsString(
                  new DigitalSpecimenEvent(enrichmentServices(false), digitalSpecimen)));
          processDigitalMediaObjects(physicalSpecimenId, unit);
        } catch (DiSSCoDataException e) {
          log.error("Encountered data issue with record: {}", unitAttributes, e);
        }
      } else {
        log.warn("Ignoring record with id: {} as we cannot determine the physicalSpecimenIdType",
            termMapper.retrieveTerm(new PhysicalSpecimenId(), unitAttributes, false));
      }
    } else {
      log.info("Record with id: {} and record basis: {} is ignored ",
          termMapper.retrieveTerm(new PhysicalSpecimenId(), unitAttributes, false),
          unit.getRecordBasis());
    }
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
    data.remove("ods:taxonIdentificationIndex");
    data.fields().forEachRemaining(field -> {
      if (field.getKey().startsWith("abcd:multiMediaObjects")) {
        multiMediaFields.add(field.getKey());
      }
    });
    data.remove(multiMediaFields);
    return data;
  }

  private void processDigitalMediaObjects(String physicalSpecimenId, Unit unit)
      throws JsonProcessingException {
    if (unit.getMultiMediaObjects() != null && !unit.getMultiMediaObjects().getMultiMediaObject()
        .isEmpty()) {
      for (MultiMediaObject media : unit.getMultiMediaObjects().getMultiMediaObject()) {
        processDigitalMediaObject(physicalSpecimenId, media);
      }
    }
  }

  private void processDigitalMediaObject(String physicalSpecimenId, MultiMediaObject media)
      throws JsonProcessingException {
    var attributes = getData(mapper.valueToTree(media));
    var digitalMediaObject = new DigitalMediaObject(
        termMapper.retrieveTerm(new MediaType(), attributes, false),
        physicalSpecimenId,
        harmonizeMedia(attributes),
        attributes
    );
    log.debug("Result digital media object: {}", digitalMediaObject);
    kafkaService.sendMessage("digital-media-object",
        mapper.writeValueAsString(
            new DigitalMediaObjectEvent(enrichmentServices(true), digitalMediaObject)));
  }

  private JsonNode harmonizeMedia(JsonNode mediaAttributes) {
    var attributes = mapper.createObjectNode();
    attributes.put(AccessUri.TERM,
        termMapper.retrieveTerm(new AccessUri(), mediaAttributes, false));
    attributes.put(SourceSystemId.TERM, webClientProperties.getSourceSystemId());
    attributes.put(Format.TERM, termMapper.retrieveTerm(new Format(), mediaAttributes, false));
    attributes.put(License.TERM, termMapper.retrieveTerm(new License(), mediaAttributes, false));
    return attributes;
  }

  private List<String> enrichmentServices(boolean multiMediaObject) {
    if (enrichmentProperties.getList() != null) {
      return enrichmentProperties.getList().stream()
          .filter(e -> e.isImageOnly() == multiMediaObject).map(Enrichment::getName).toList();
    } else {
      return Collections.emptyList();
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
    map.put(LIMIT, webClientProperties.getItemsPerRequest());
    map.put(START_AT, 0);
    return map;
  }

  private void updateStartAtParameter(Map<String, Object> templateProperties) {
    templateProperties.put(START_AT,
        ((int) templateProperties.get(START_AT) + (int) templateProperties.get(LIMIT)));
  }

  private boolean isStartElement(XMLEvent element, String field) {
    if (element != null) {
      return element.isStartElement() && element.asStartElement().getName().getLocalPart()
          .equals(field);
    } else {
      return false;
    }
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
