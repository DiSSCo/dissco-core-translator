package eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
public abstract class AbstractChronoStratigraphy extends Term {

  private static final Pair<String, String> ABCD_DIVISION =
      Pair.of(
          "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/",
          "/chronoStratigraphicDivision");
  private static final Pair<String, String> ABCD_VALUE =
      Pair.of(
          "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/",
          "/chronostratigraphicName");

  protected String searchABCDChronostratigraphy(JsonNode unit, List<String> divisionSearched) {
    for (var divisionSearch : divisionSearched) {
      var iterateOverElements = true;
      var numberFound = 0;
      while (iterateOverElements) {
        var divisionNode = unit.get(
            ABCD_DIVISION.getLeft() + numberFound + ABCD_DIVISION.getRight());
        if (divisionNode != null) {
          var division = divisionNode.asText();
          if (division.equalsIgnoreCase(divisionSearch)
              && unit.get(ABCD_VALUE.getLeft() + numberFound + ABCD_VALUE.getRight()) != null) {
            return unit.get(ABCD_VALUE.getLeft() + numberFound + ABCD_VALUE.getRight()).asText();
          }
          numberFound++;
        } else {
          iterateOverElements = false;
        }
      }
    }
    log.debug("No stratigraphy found for division: {}", divisionSearched);
    return null;
  }

}
