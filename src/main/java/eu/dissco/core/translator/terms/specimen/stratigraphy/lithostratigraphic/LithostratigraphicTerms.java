package eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class LithostratigraphicTerms extends Term {

  public static final String TERM = DWC_PREFIX + "lithostratigraphicTerms";

  private final List<String> dwcaTerms = List.of(TERM);
  // Take the first lithostratigraphicAttribution we can find
  private final List<String> abcdTerms = List.of(
      "abcd:gathering/stratigraphy/lithostratigraphicTerms/lithostratigraphicTerm/0/term");

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
