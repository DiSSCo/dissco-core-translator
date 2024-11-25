package eu.dissco.core.translator.terms.specimen.location;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class MinimumDistanceAboveSurfaceInMeters extends AbstractMeterTerm {

  public static final String TERM = DWC_PREFIX + "minimumDistanceAboveSurfaceInMeters";

  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of(
      "abcd:gathering/height/measurementOrFactAtomised/lowerValue/value");

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
