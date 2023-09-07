package eu.dissco.core.translator.terms;


import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
public abstract class Term {

  protected static final String ODS_PREFIX = "ods:";
  protected static final String DWC_PREFIX = "dwc:";
  protected static final Pair<String, String> ABCD_NAMED_AREA_KEY =
      Pair.of(
          "abcd:gathering/namedAreas/namedArea/",
          "/areaClass/value");
  protected static final Pair<String, String> ABCD_NAMED_AREA_VALUE =
      Pair.of(
          "abcd:gathering/namedAreas/namedArea/",
          "/areaName/value");
  private static final String MESSAGE = "No specific attributes retrieve specified for field: {}";

  protected String searchJsonForStringTerm(JsonNode attributes, List<String> originalTerms, Class clazz) {
    for (var originalTerm : originalTerms) {
      var valueNode = attributes.get(originalTerm);
      if (valueNode != null && valueNode.isTextual()) {
        if (clazz.equals(Boolean.class)){
          
        }
        return attributes.get(originalTerm).asText();
      }
    }
    log.debug("Term not found in any of these search fields: {}", originalTerms);
    return null;
  }

  protected String searchJsonForLongTerm(JsonNode attributes, List<String> originalTerms) {
    for (var originalTerm : originalTerms) {
      var valueNode = attributes.get(originalTerm);
      if (valueNode != null && valueNode.isLong()) {
        var longValue = attributes.get(originalTerm).asLong();
        return String.valueOf(longValue);
      }
    }
    log.debug("Term not found in any of these search fields: {}", originalTerms);
    return null;
  }

  protected String combineABCDTerms(JsonNode unit, List<String> abcdTerms) {
    var builder = new StringBuilder();
    for (var abcdTerm : abcdTerms) {
      if (unit.get(abcdTerm) != null) {
        if (builder.length() != 0) {
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

  public abstract String getTerm();

  public String retrieveFromABCD(JsonNode unit) {
    log.debug(MESSAGE, getTerm());
    return null;
  }

  public String retrieveFromABCD(JsonNode dataset, JsonNode unit) {
    log.debug(MESSAGE, getTerm());
    return null;
  }

  public String retrieveFromDWCA(JsonNode unit) {
    log.debug(MESSAGE, getTerm());
    return null;
  }

  public DigitalSpecimen retrieveFromDWCANew(DigitalSpecimen ds, JsonNode unit) {
    log.debug(MESSAGE, getTerm());
    return ds;
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
}
