package eu.dissco.core.translator.terms.specimen.event;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class EventRemarks extends Term {

  public static final String TERM = DWC_PREFIX + "eventRemarks";
  private final List<String> dwcaTerms = List.of(TERM, "dwc:occurrenceRemarks");
  private final List<String> abcdUnitTerms = List.of("abcd:notes/value");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var builder = new StringBuilder();
    for (var term : dwcaTerms) {
      if (unit.get(term) != null) {
        if (!builder.isEmpty()) {
          builder.append(" | ");
        }
        builder.append(unit.get(term).asText());
      }
    }
    if (builder.isEmpty()) {
      return null;
    } else {
      return builder.toString();
    }
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return searchJsonForTerm(unit, abcdUnitTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}

