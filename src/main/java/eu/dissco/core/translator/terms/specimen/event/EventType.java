package eu.dissco.core.translator.terms.specimen.event;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class EventType extends Term {

  public static final String TERM = DWC_PREFIX + "eventType";

  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var eventType = super.searchJsonForTerm(unit, dwcaTerms);
    if (eventType == null) {
      eventType = "Collecting Event";
    }
    return eventType;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
