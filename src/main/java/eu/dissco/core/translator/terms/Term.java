package eu.dissco.core.translator.terms;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.Identification;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
public abstract class Term {

  protected static final String ODS_PREFIX = "ods:";
  protected static final String DWC_PREFIX = "dwc:";
  protected static final String DCTERMS_PREFIX = "dcterms:";
  protected static final String CHRONO_PREFIX = "chrono:";
  protected static final Pair<String, String> ABCD_NAMED_AREA_KEY =
      Pair.of(
          "abcd:gathering/namedAreas/namedArea/",
          "/areaClass/value");
  protected static final Pair<String, String> ABCD_NAMED_AREA_VALUE =
      Pair.of(
          "abcd:gathering/namedAreas/namedArea/",
          "/areaName/value");
  private static final String MESSAGE = "No specific attributes retrieve specified for field: {}";

  protected String searchJsonForTerm(JsonNode attributes, List<String> originalTerms) {
    for (var originalTerm : originalTerms) {
      var valueNode = attributes.get(originalTerm);
      if (valueNode != null) {
        if (valueNode.isTextual()) {
          return attributes.get(originalTerm).asText();
        } else if (valueNode.isLong()) {
          var longValue = attributes.get(originalTerm).asLong();
          return String.valueOf(longValue);
        } else if (valueNode.isBoolean()) {
          var boolValue = attributes.get(originalTerm).asBoolean();
          return String.valueOf(boolValue);
        } else if (valueNode.isDouble() || valueNode.isBigDecimal()) {
          var doubleValue = attributes.get(originalTerm).asDouble();
          return String.valueOf(doubleValue);
        } else if (valueNode.isInt() || valueNode.isBigInteger()) {
          var intValue = attributes.get(originalTerm).asInt();
          return String.valueOf(intValue);
        }
      }
    }
    log.debug("Term not found in any of these search fields: {}", originalTerms);
    return null;
  }

  protected String combineABCDTerms(JsonNode unit, List<String> abcdTerms) {
    var builder = new StringBuilder();
    for (var abcdTerm : abcdTerms) {
      if (unit.get(abcdTerm) != null) {
        if (!builder.isEmpty()) {
          builder.append(" | ");
        }
        builder.append(unit.get(abcdTerm).asText());
      }
    }
    if (builder.isEmpty()) {
      return null;
    } else {
      return builder.toString();
    }
  }

  protected JsonNode getSubJsonAbcd(ObjectMapper mapper, JsonNode data, int count, String path) {
    var subNode = mapper.createObjectNode();
    data.properties().forEach(field -> {
      if (field.getKey().startsWith(path + count)) {
        subNode.set(
            field.getKey().replace(path + count + "/", ""),
            field.getValue());
      }
    });
    return subNode;
  }

  public abstract String getTerm();

  public String retrieveFromABCD(JsonNode unit) {
    log.debug(MESSAGE, getTerm());
    return null;
  }

  public String retrieveFromDWCA(JsonNode unit) {
    log.debug(MESSAGE, getTerm());
    return null;
  }

  protected String searchABCDSplitTerms(JsonNode unit, List<String> searchTerms,
      Pair<String, String> key, Pair<String, String> value) {
    for (var divisionSearch : searchTerms) {
      var iterateOverElements = true;
      var numberFound = 0;
      while (iterateOverElements) {
        var divisionNode = unit.get(
            key.getLeft() + numberFound + key.getRight());
        if (divisionNode != null) {
          var division = divisionNode.asText();
          if (division.equalsIgnoreCase(divisionSearch)
              && unit.get(value.getLeft() + numberFound + value.getRight()) != null) {
            return unit.get(value.getLeft() + numberFound + value.getRight()).asText();
          }
          numberFound++;
        } else {
          iterateOverElements = false;
        }
      }
    }
    log.debug("No value found for division: {}", searchTerms);
    return null;
  }

  protected Identification retrieveAcceptedIdentification(DigitalSpecimen ds) {
    if (ds.getOdsHasIdentifications() != null && !ds.getOdsHasIdentifications().isEmpty()) {
      if (ds.getOdsHasIdentifications().size() == 1) {
        return ds.getOdsHasIdentifications().get(0);
      }
      for (Identification identification : ds.getOdsHasIdentifications()) {
        if (Boolean.TRUE.equals(identification.getOdsIsVerifiedIdentification())) {
          return identification;
        }
      }
    }
    return null;
  }
}
