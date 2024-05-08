package eu.dissco.core.translator.terms.specimen.location;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class MinimumDepthInMeters extends Term {

  public static final String TERM = DWC_PREFIX + "minimumDepthInMeters";

  private final List<String> dwcaTerms = List.of(TERM, VerbatimDepth.TERM);
  private final List<String> abcdTerms = List.of(
      "abcd:gathering/depth/measurementOrFactAtomised/lowerValue/value",
      "abcd:gathering/depth/measurementOrFactText/value");

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
