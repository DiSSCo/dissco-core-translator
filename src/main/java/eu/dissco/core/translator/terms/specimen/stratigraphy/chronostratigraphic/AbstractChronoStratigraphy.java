package eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractChronoStratigraphy extends Term {

  private static final Pair<String, String> ABCD_DIVISION =
      Pair.of(
          "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/",
          "/chronoStratigraphicDivision");
  private static final Pair<String, String> ABCD_VALUE =
      Pair.of(
          "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/",
          "/chronostratigraphicName");

  protected String searchABCDSplitTerms(JsonNode unit, List<String> searchTerms) {
    return searchABCDSplitTerms(unit, searchTerms, ABCD_DIVISION, ABCD_VALUE);
  }

}
