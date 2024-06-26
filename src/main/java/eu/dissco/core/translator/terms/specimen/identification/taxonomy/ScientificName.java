package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScientificName extends AbstractTaxonomy {

  public static final String TERM = DWC_PREFIX + "scientificName";
  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of(
      "result/taxonIdentified/scientificName/fullScientificNameString",
      "result/mineralRockIdentified/classifiedName/fullScientificNameString",
      "result/taxonIdentified/informalNameString/value",
      "result/taxonIdentified/scientificName/nameAtomised/zoological/namedIndividual");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return searchJsonForTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
