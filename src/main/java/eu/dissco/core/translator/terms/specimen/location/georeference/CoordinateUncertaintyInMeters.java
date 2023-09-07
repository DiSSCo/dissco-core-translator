package eu.dissco.core.translator.terms.specimen.location.georeference;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class CoordinateUncertaintyInMeters extends Term {

  public static final String TERM = DWC_PREFIX + "coordinateUncertaintyInMeters";
  private static final String ABCD_TERM =
      "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/coordinateErrorDistanceInMeters";
  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var latitude = unit.get(ABCD_TERM);
    if (latitude != null && (latitude.isDouble() || latitude.isBigDecimal())) {
      return String.valueOf(latitude.asDouble());
    }
    return null;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
