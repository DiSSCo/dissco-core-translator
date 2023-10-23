package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class SpecificEpithet extends AbstractTaxonomy {

  public static final String TERM = DWC_PREFIX + "specificEpithet";
  private final List<String> abcdTerms = List.of(
      "result/taxonIdentified/scientificName/nameAtomised/botanical/firstEpithet",
      "result/taxonIdentified/scientificName/nameAtomised/zoological/speciesEpithet",
      "result/taxonIdentified/scientificName/nameAtomised/bacterial/speciesEpithet");
  private final List<String> dwcaTerms = List.of(TERM);

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
