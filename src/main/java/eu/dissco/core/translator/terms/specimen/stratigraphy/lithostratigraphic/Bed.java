package eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class Bed extends Term {

  public static final String TERM = DWC_PREFIX + "bed";

  private final List<String> dwcaTerms = List.of(TERM);
  // Take the first lithostratigraphicAttribution we can find
  private final List<String> abcdTerms = List.of(
      "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/lithostratigraphicAttributions/lithostratigraphicAttribution/0/bed");

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
