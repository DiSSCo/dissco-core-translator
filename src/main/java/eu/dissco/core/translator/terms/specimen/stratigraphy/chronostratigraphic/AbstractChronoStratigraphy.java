package eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractChronoStratigraphy extends Term {

  private static final String ABCD_DIVISION =
      "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/%s/chronoStratigraphicDivision";
  private static final String ABCD_VALUE =
      "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/%s/chronostratigraphicName";

  protected String searchABCDChronostratigraphy(JsonNode unit, List<String> divisionSearched) {
    for (var divisionSearch : divisionSearched) {
      var iterateOverElements = true;
      var numberFound = 0;
      while (iterateOverElements) {
        var divisionNode = unit.get(String.format(ABCD_DIVISION, numberFound));
        if (divisionNode != null) {
          var division = divisionNode.asText();
          if (division.equalsIgnoreCase(divisionSearch)
              && unit.get(String.format(ABCD_VALUE, numberFound)) != null) {
            return unit.get(String.format(ABCD_VALUE, numberFound)).asText();
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
