package eu.dissco.core.translator.terms.specimen.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class Family extends AbstractTaxonomy {

  public static final String TERM = DWC_PREFIX + "family";

  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchABCDSplitTerms(unit, List.of("familia", "family"));
  }

  @Override
  public String getTerm() {
    return TERM;
  }

}
