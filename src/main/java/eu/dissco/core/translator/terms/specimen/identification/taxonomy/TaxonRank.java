package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class TaxonRank extends AbstractTaxonomy {

  public static final String TERM = DWC_PREFIX + "taxonRank";

  private final List<String> dwcaTerms = List.of(TERM, VerbatimTaxonRank.TERM);
  private final List<String> abcdTerms = List.of(
      "result/taxonIdentified/scientificName/nameAtomised/botanical/rank");

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
