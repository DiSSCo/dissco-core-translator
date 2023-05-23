package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class CollectingNumber extends Term {

  public static final String TERM = ODS_PREFIX + "collectingNumber";

  private final List<String> dwcaTerms = List.of("dwc:recordNumber");
  private final List<String> abcdTerms = List.of("abcd:collectorsFieldNumber",
      "abcd:gathering/code");

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
