package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
public class SpecimenName extends Term {

  public static final String TERM = ODS_PREFIX + "specimenName";
  private final List<String> dwcaTerms = List.of("dwc:scientificName");
  private final Pair<String, String> abcdPreferredFlag = Pair.of(
      "abcd:identifications/identification/", "/preferredFlag");
  private final List<Pair<String, String>> abcdTerms = List.of(
      Pair.of("abcd:identifications/identification/",
          "/result/taxonIdentified/scientificName/fullScientificNameString"),
      Pair.of("abcd-efg:identifications/identification/",
          "/result/mineralRockIdentified/classifiedName/fullScientificNameString")
  );

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var numberFound = 0;
    while (true) {
      if (unit.get(getStringAtIndex(abcdPreferredFlag, numberFound)) != null) {
        var optionalResult = checkPreferredFlag(unit, numberFound);
        if (optionalResult.isPresent()) {
          return optionalResult.get();
        } else {
          numberFound++;
        }
      } else {
        return getFirstName(unit, abcdTerms);
      }
    }
  }

  private Optional<String> checkPreferredFlag(JsonNode unit, int numberFound) {
    var preferred = unit.get(getStringAtIndex(abcdPreferredFlag, numberFound)).asBoolean();
    if (preferred) {
      for (var abcdTerm : abcdTerms) {
        var attribute = (unit.get(getStringAtIndex(abcdTerm, numberFound)));
        if (attribute != null) {
          return Optional.of(attribute.asText());
        }
      }
    }
    return Optional.empty();
  }

  private String getStringAtIndex(Pair<String, String> string, int numberFound) {
    return string.getLeft() + numberFound + string.getRight();
  }

  private String getFirstName(JsonNode unit, List<Pair<String, String>> abcdTerms) {
    for (var abcdTerm : abcdTerms) {
      var attribute = unit.get(getStringAtIndex(abcdTerm, 0));
      if (attribute != null) {
        return attribute.asText();
      }
    }
    return null;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
