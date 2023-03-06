package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

public class Collector extends Term {

  public static final String TERM = ODS_PREFIX + "collector";

  private final List<String> dwcaTerms = List.of("dwc:recordedBy", "dwc:recordedByID");
  private final List<String> abcdTerms = List.of(
      "abcd:gathering/agents/gatheringAgent/%s/person/fullName",
      "abcd:gathering/agents/gatheringAgent/%s/person/agentText");

  @Override
  public String retrieveFromDWCA(ArchiveFile archiveFile, Record rec) {
    return super.searchDWCAForTerm(archiveFile, rec, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var value = combinePossibleValues(unit);
    if (value == null){
      if (unit.get("abcd:gathering/agents/gatheringAgentsText") != null){
        return unit.get("abcd:gathering/agents/gatheringAgentsText").asText();
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
        var jsonValue = unit.get(String.format(abcdTerm, numberFound));
        if (jsonValue != null) {
          string = jsonValue.asText();
          break;
        }
      }
      if (string != null) {
        if (numberFound == 0){
          builder.append(string);
        } else {
          builder.append(" | ").append(string);
        }
        numberFound++;
      } else {
        iterateOverElements = false;
      }
    }
    if (builder.length() != 0){
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
