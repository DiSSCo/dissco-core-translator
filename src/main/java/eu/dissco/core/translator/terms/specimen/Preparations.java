package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class Preparations extends Term {

  public static final String TERM = DWC_PREFIX + "preparations";
  private final List<String> dwcaTerms = List.of("dwc:preparations");
  private final List<String> abcdTerms = List.of("abcd:kindOfUnit/0/value", "abcd:specimenUnit/preparations/preparation/0/preparationType/value");

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
