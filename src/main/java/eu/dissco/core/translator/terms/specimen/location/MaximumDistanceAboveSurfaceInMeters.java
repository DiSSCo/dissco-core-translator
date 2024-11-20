package eu.dissco.core.translator.terms.specimen.location;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class MaximumDistanceAboveSurfaceInMeters extends AbstractMeterTerm {

  public static final String TERM = DWC_PREFIX + "maximumDistanceAboveSurfaceInMeters";

  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of(
      "abcd:gathering/height/measurementOrFactAtomised/upperValue/value");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var rawResult = super.searchJsonForTerm(unit, dwcaTerms);
    if (rawResult != null) {
      return sanitizeInput(rawResult);
    } else {
      return null;
    }
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var rawResult = super.searchJsonForTerm(unit, abcdTerms);
    if (rawResult != null) {
      return sanitizeInput(rawResult);
    } else {
      return null;
    }
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
