package eu.dissco.core.translator.terms.specimen.stratigraphy.biostratigraphic;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class LowestBiostratigraphicZone extends Term {

  public static final String TERM = DWC_PREFIX + "lowestBiostratigraphicZone";

  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of(
      "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/biostratigraphicAttributionsType/biostratigraphicAttribution/0/zonalFossilType",
      "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/biostratigraphicAttributionsType/biostratigraphicAttribution/0/fossilZoneName",
      "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/biostratigraphicAttributionsType/biostratigraphicAttribution/0/fossilSubzoneName");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.combineABCDTerms(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }

}
