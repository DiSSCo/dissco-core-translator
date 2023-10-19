package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class SubTribe extends AbstractTaxonomy {

  public static final String TERM = DWC_PREFIX + "subtribe";

  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdSplitTerms = List.of("subtribus", "subtribe");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchABCDSplitTerms(unit, abcdSplitTerms);
  }
  @Override
  public String getTerm() {
    return TERM;
  }
}
