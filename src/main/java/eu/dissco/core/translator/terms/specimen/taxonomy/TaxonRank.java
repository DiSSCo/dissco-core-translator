package eu.dissco.core.translator.terms.specimen.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class TaxonRank extends AbstractTaxonomy {

  public static final String TERM = DWC_PREFIX + "taxonRank";

  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.getTaxonFromDWCA(unit, dwcaTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
