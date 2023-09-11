package eu.dissco.core.translator.terms.specimen.location;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class LocationRemarks extends Term {
  public static final String TERM = DWC_PREFIX + "locationRemarks";

  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of("abcd:gathering/areaDetail/value");

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
