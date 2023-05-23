package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class DateCollected extends Term {

  public static final String TERM = ODS_PREFIX + "dateCollected";

  private final List<String> dwcaTerms = List.of("dwc:eventDate");
  private final List<String> abcdTerms = List.of("abcd:gathering/dateTime/isodateTimeBegin",
      "abcd:gathering/dateTime/isodateTimeEnd", "abcd:gathering/dateTime/dateText");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
