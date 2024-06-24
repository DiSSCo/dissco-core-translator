package eu.dissco.core.translator.terms.specimen.event;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class EventDate extends Term {

  public static final String TERM = DWC_PREFIX + "eventDate";

  private final List<String> dwcaTerms = List.of(TERM, VerbatimEventDate.TERM);
  private final List<String> abcdTerms = List.of("abcd:gathering/dateTime/isodateTimeBegin",
      "abcd:gathering/dateTime/isodateTimeEnd", "abcd:gathering/dateTime/dateText");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchJsonForTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
