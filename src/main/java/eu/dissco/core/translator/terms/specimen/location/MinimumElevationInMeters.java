package eu.dissco.core.translator.terms.specimen.location;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class MinimumElevationInMeters extends AbstractMeterTerm {

  public static final String TERM = DWC_PREFIX + "minimumElevationInMeters";

  private final List<String> dwcaTerms = List.of(TERM, VerbatimElevation.TERM);
  private final List<String> abcdTerms = List.of(
      "abcd:gathering/altitude/measurementOrFactAtomised/lowerValue/value",
      "abcd:gathering/altitude/measurementOrFactText/value");

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
