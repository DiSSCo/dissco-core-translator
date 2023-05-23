package eu.dissco.core.translator.terms.specimen.location;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class DecimalLatitude extends Term {

  public static final String TERM = DWC_PREFIX + "decimalLatitude";
  private static final String ABCD_TERM = "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/latitudeDecimal";
  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var latitude = unit.get(ABCD_TERM);
    if (latitude != null && latitude.isDouble()) {
      return String.valueOf(latitude.asDouble());
    }
    return null;
  }

  @Override
  public String getTerm() {
    return TERM;
  }

}
