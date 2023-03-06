package eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractChronoStratigraphy extends Term {

  private final String abcdDivision =
      "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/%s/chronoStratigraphicDivision";
  private final String abcdValue =
      "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/%s/chronostratigraphicName";

  protected String searchABCDChronostratigraphy(JsonNode unit, List<String> divisionSearched) {
    for (var divisionSearch : divisionSearched) {
      var iterateOverElements = true;
      var numberFound = 0;
      while (iterateOverElements) {
        var divisionNode = unit.get(String.format(abcdDivision, numberFound));
        if (divisionNode != null) {
          var division = divisionNode.asText();
          if (division.equalsIgnoreCase(divisionSearch)
              && unit.get(String.format(abcdValue, numberFound)) != null) {
            return unit.get(String.format(abcdValue, numberFound)).asText();
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
