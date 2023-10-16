package eu.dissco.core.translator.terms.specimen.location;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class MinimumElevationInMeters extends Term {

  public static final String TERM = DWC_PREFIX + "minimumElevationInMeters";

  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of(
      "abcd:gathering/altitude/measurementOrFactAtomised/lowerValue/value", "abcd:gathering/altitude/measurementOrFactText/value");

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
