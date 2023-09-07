package eu.dissco.core.translator.terms.specimen.location.georeference;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class GeoreferenceProtocol extends Term {

  public static final String TERM = DWC_PREFIX + "georeferenceProtocol";

  private final List<String> dwcaTerms = List.of(TERM);

  private final List<String> abcdTerms = List.of(
      "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinateMethod");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
