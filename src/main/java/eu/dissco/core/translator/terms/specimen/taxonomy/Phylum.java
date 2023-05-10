package eu.dissco.core.translator.terms.specimen.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class Phylum extends AbstractTaxonomy {

  public static final String TERM = DWC_PREFIX + "phylum";

  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchABCDSplitTerms(unit, List.of("phylum", "divisio"));
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
