package eu.dissco.core.translator.terms.specimen.location;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class MaximumDepthInMeters extends AbstractMeterTerm {

  public static final String TERM = DWC_PREFIX + "maximumDepthInMeters";

  private final List<String> dwcaTerms = List.of(TERM, VerbatimDepth.TERM);
  private final List<String> abcdTerms = List.of(
      "abcd:gathering/depth/measurementOrFactAtomised/upperValue/value",
      "abcd:gathering/depth/measurementOrFactText/value");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var rawResult = super.searchJsonForTerm(unit, dwcaTerms);
    return sanitizeInput(rawResult);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var rawResult = super.searchJsonForTerm(unit, abcdTerms);
    return sanitizeInput(rawResult);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
