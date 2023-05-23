package eu.dissco.core.translator.terms.specimen.location;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class DecimalLongitude extends Term {

  public static final String TERM = DWC_PREFIX + "decimalLongitude";
  private static final String ABCD_TERM = "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/longitudeDecimal";
  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var longitude = unit.get(ABCD_TERM);
    if (longitude != null && longitude.isDouble()) {
      return String.valueOf(longitude.asDouble());
    }
    return null;
  }

  @Override
  public String getTerm() {
    return TERM;
  }

}
