package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class RecordedBy extends Term {

  public static final String TERM = DWC_PREFIX + "recordedBy";

  private final List<String> dwcaTerms = List.of("dwc:recordedBy");
  private final List<Pair<String, String>> abcdTerms = List.of(
      Pair.of("abcd:gathering/agents/gatheringAgent/", "/person/fullName"),
      Pair.of("abcd:gathering/agents/gatheringAgent/", "/person/agentText"));

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var value = combinePossibleValues(unit);
    if (value == null) {
      if (unit.get("abcd:gathering/agents/gatheringAgentsText/value") != null) {
        return unit.get("abcd:gathering/agents/gatheringAgentsText/value").asText();
      } else {
        return null;
      }
    } else {
      return value;
    }
  }

  private String combinePossibleValues(JsonNode unit) {
    var builder = new StringBuilder();
    var iterateOverElements = true;
    var numberFound = 0;
    while (iterateOverElements) {
      String string = null;
      for (var abcdTerm : abcdTerms) {
        var jsonValue = unit.get(abcdTerm.getLeft() + numberFound + abcdTerm.getRight());
        if (jsonValue != null) {
          string = jsonValue.asText();
          break;
        }
      }
      if (string != null) {
        if (numberFound == 0) {
          builder.append(string);
        } else {
          builder.append(" | ").append(string);
        }
        numberFound++;
      } else {
        iterateOverElements = false;
      }
    }
    if (!builder.isEmpty()) {
      return builder.toString();
    } else {
      return null;
    }
  }


  @Override
  public String getTerm() {
    return TERM;
  }
}
