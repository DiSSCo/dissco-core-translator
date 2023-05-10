package eu.dissco.core.translator.terms.specimen.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
public class SpecimenName extends AbstractTaxonomy {

  public static final String TERM = ODS_PREFIX + "specimenName";
  private final List<String> dwcaTerms = List.of("dwc:scientificName");
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
    var identificationIndex = getIdentificationIndex(unit);
    var terms = abcdTerms.stream()
        .map(abcdTerm -> getStringAtIndex(abcdTerm, Integer.parseInt(identificationIndex)))
        .toList();
    return searchJsonForTerm(unit, terms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
