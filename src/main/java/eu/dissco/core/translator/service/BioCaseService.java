package eu.dissco.core.translator.service;


import static efg.RecordBasisEnum.FOSSILE_SPECIMEN;
import static efg.RecordBasisEnum.FOSSIL_SPECIMEN;
import static efg.RecordBasisEnum.LIVING_SPECIMEN;
import static efg.RecordBasisEnum.METEORITE_SPECIMEN;
import static efg.RecordBasisEnum.MINERAL_SPECIMEN;
import static efg.RecordBasisEnum.OTHER_SPECIMEN;
import static efg.RecordBasisEnum.PRESERVED_SPECIMEN;
import static efg.RecordBasisEnum.ROCK_SPECIMEN;
import static efg.RecordBasisEnum.UNSPECIFIED;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import efg.DataSets;
import efg.EarthScienceSpecimenType;
import efg.MultiMediaObject;
import efg.RecordBasisEnum;
import efg.Unit;
import eu.dissco.core.translator.Profiles;
import eu.dissco.core.translator.component.MappingComponent;
import eu.dissco.core.translator.domain.DigitalMediaObject;
import eu.dissco.core.translator.domain.DigitalMediaObjectEvent;
import eu.dissco.core.translator.domain.DigitalSpecimen;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.domain.Enrichment;
import eu.dissco.core.translator.properties.EnrichmentProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.SourceSystemRepository;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
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

@Slf4j
@Service
@Profile(Profiles.BIO_CASE)
@RequiredArgsConstructor
public class BioCaseService implements WebClientService {

  private static final String START_AT = "startAt";
  private static final String LIMIT = "limit";
  private static final String ABCD = "abcd:";
  private static final String ABCDEFG = "abcd-efg:";
  private static final List<RecordBasisEnum> ALLOWED_RECORD_BASIS = List.of(PRESERVED_SPECIMEN,
      LIVING_SPECIMEN, FOSSILE_SPECIMEN, OTHER_SPECIMEN, UNSPECIFIED, FOSSIL_SPECIMEN,
      ROCK_SPECIMEN, METEORITE_SPECIMEN, MINERAL_SPECIMEN);
  private static final String PHYSICAL_SPECIMEN_ID = "physical_specimen_id";

  private final ObjectMapper mapper;
  private final WebClientProperties webClientProperties;
  private final WebClient webClient;
  private final SourceSystemRepository repository;
  private final Configuration configuration;
  private final XMLInputFactory xmlFactory;
  private final MappingComponent mapping;
  private final KafkaService kafkaService;
  private final EnrichmentProperties enrichmentProperties;

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
            .bodyToMono(String.class).map(this::mapToDarwin).toFuture().get();
        if (finished) {
          log.info("Unable to get records from xml");
          finished = true;
        }
      } catch (InterruptedException | ExecutionException e) {
        log.error("Failed to get response from uri", e);
        Thread.currentThread().interrupt();
        return;
      }
      updateStartAtParameter(templateProperties);
    }
  }

  private boolean mapToDarwin(String xml) {
    var recordCount = 0;
    try {
      var xmlEventReader = xmlFactory.createXMLEventReader(new StringReader(xml));
      while (xmlEventReader.hasNext()) {
        var element = xmlEventReader.nextEvent();
        if (isStartElement(element, "content")) {
          recordCount = Integer.parseInt(
              element.asStartElement().getAttributeByName(new QName("recordCount")).getValue());
        }
        retrieveUnitData(xmlEventReader);
      }
      return recordCount % webClientProperties.getItemsPerRequest() != 0;
    } catch (XMLStreamException | JsonProcessingException e) {
      log.info("Error converting response tot XML", e);
    }
    return false;
  }

  private void retrieveUnitData(XMLEventReader xmlEventReader)
      throws XMLStreamException, JsonProcessingException {
    mapper.setSerializationInclusion(Include.NON_NULL);
    if (isStartElement(xmlEventReader.peek(), "DataSets")) {
      try {
        mapABCD206(xmlEventReader);
      } catch (JAXBException e) {
        e.printStackTrace();
      }
    }
  }

  private void mapABCD206(XMLEventReader xmlEventReader)
      throws JAXBException, JsonProcessingException {
    var context = JAXBContext.newInstance(DataSets.class);
    var datasetsMarshaller = context.createUnmarshaller().unmarshal(xmlEventReader, DataSets.class);
    var datasets = datasetsMarshaller.getValue().getDataSet().get(0);
    for (var unit : datasets.getUnits().getUnit()) {
      var efg = getEfg(unit);
      var unitData = getData(mapper.valueToTree(unit));
      if (efg != null) {
        addEfgFields(unitData, efg);
      }
      if (unit.getRecordBasis() != null && ALLOWED_RECORD_BASIS.contains(unit.getRecordBasis())) {
        var organizationId = getProperty("organization_id", unitData);
        var physicalSpecimenIdType = getProperty("physical_specimen_id_type", unitData);
        if (physicalSpecimenIdType != null) {
          var physicalSpecimenId = getPhysicalSpecimenId(physicalSpecimenIdType, organizationId,
              unitData);
          var digitalSpecimen = new DigitalSpecimen(
              getProperty("type", unitData),
              physicalSpecimenId,
              physicalSpecimenIdType,
              getProperty("physical_specimen_collection", unitData),
              getProperty("specimen_name", unitData),
              organizationId,
              datasets.getDatasetGUID(),
              webClientProperties.getSourceSystemId(),
              removeMappedFields(unitData),
              unitData,
              null
          );
          log.info("Result digital Specimen: {}", digitalSpecimen);
          kafkaService.sendMessage("digital-specimen",
              mapper.writeValueAsString(
                  new DigitalSpecimenEvent(enrichmentServices(false), digitalSpecimen)));
          processDigitalMediaObjects(physicalSpecimenId, unit);
        } else {
          log.warn("Ignoring record with id: {} as we cannot determine the physicalSpecimenIdType",
              getProperty(PHYSICAL_SPECIMEN_ID, unitData));
        }
      } else {
        log.info("Record with id: {} and record basis: {} is ignored ",
            getProperty(PHYSICAL_SPECIMEN_ID, unitData), unit.getRecordBasis());
      }
    }
  }

  private EarthScienceSpecimenType getEfg(Unit unit) {
    try {
      var context = JAXBContext.newInstance(EarthScienceSpecimenType.class);
      var node = ((Node) unit.getUnitExtension());
      if (node != null) {
        var document = node.getOwnerDocument();
        try {
          Source xmlSource = new DOMSource(document);
          var xmlEventReader = xmlFactory.createXMLEventReader(xmlSource);
          while (xmlEventReader.hasNext()) {
            xmlEventReader.nextEvent();
            if (isStartElement(xmlEventReader.peek(), "EarthScienceSpecimen")) {
              try {
                return context.createUnmarshaller()
                    .unmarshal(xmlEventReader, EarthScienceSpecimenType.class)
                    .getValue();
              } catch (JAXBException e) {
                e.printStackTrace();
              }
            }
          }
        } catch (XMLStreamException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  private void addEfgFields(ObjectNode unitData, EarthScienceSpecimenType efg) {
    StringBuilder prefix = new StringBuilder();
    iterateOverNode(unitData, mapper.valueToTree(efg), ABCDEFG, prefix);
    unitData.remove("abcd:unitExtension");
  }

  private JsonNode removeMappedFields(JsonNode unitData) {
    var objectNode = (ObjectNode) unitData.deepCopy();
    objectNode.remove(mapping.getFieldMappings().values());
    objectNode.remove(mapping.getDefaultMappings().values());
    return objectNode;
  }

  private String getPhysicalSpecimenId(String physicalSpecimenIdType, String organizationId,
      JsonNode unitData) {
    if (physicalSpecimenIdType.equals("cetaf")) {
      return getProperty(PHYSICAL_SPECIMEN_ID, unitData);
    } else if (physicalSpecimenIdType.equals("combined")) {
      return getProperty(PHYSICAL_SPECIMEN_ID, unitData) + ":" + minifyOrganizationId(
          organizationId);
    } else {
      log.warn("Unknown physicalSpecimenIdType specified");
      return "Unknown";
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
    var data = getData(mapper.valueToTree(media));
    var digitalMediaObject = new DigitalMediaObject(
        determineType(media.getFormat()),
        physicalSpecimenId,
        media.getFileURI(),
        media.getFormat(),
        webClientProperties.getSourceSystemId(),
        removeMappedFields(data),
        data,
        "BioCase"
    );
    log.info("Result digital media object: {}", digitalMediaObject);
    kafkaService.sendMessage("digital-media-object",
        mapper.writeValueAsString(
            new DigitalMediaObjectEvent(enrichmentServices(true), digitalMediaObject)));
  }

  private String determineType(String format) {
    if (format != null) {
      format = format.toUpperCase();
      if (format.equals("JPG") || format.equals("PNG")) {
        return "2DImageObject";
      } else {
        log.warn("Unable to determine media type of digital media object");
        return null;
      }
    }
    return null;
  }

  private List<String> enrichmentServices(boolean multiMediaObject) {
    if (enrichmentProperties.getList() != null) {
      return enrichmentProperties.getList().stream()
          .filter(e -> e.isImageOnly() == multiMediaObject).map(Enrichment::getName).toList();
    } else {
      return Collections.emptyList();
    }
  }

  private String getProperty(String fieldName, JsonNode data) {
    if (mapping.getDefaultMappings().containsKey(fieldName)) {
      return mapping.getDefaultMappings().get(fieldName);
    } else if (mapping.getFieldMappings().containsKey(fieldName)) {
      var value = data.get(mapping.getFieldMappings().get(fieldName));
      if (value != null && value.isTextual()) {
        return value.asText();
      } else {
        log.warn("No values found for field: {}", fieldName);
        return null;
      }
    } else {
      log.warn("Cannot find field {}", fieldName);
      return null;
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
