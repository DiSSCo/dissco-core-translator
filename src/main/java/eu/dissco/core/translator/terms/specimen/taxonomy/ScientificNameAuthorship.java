package eu.dissco.core.translator.terms.specimen.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class ScientificNameAuthorship extends AbstractTaxonomy {

  public static final String TERM = DWC_PREFIX + "scientificNameAuthorship";

  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchABCDTerms(unit, List.of("/nameAtomised/botanical/authorTeam"));
  }

  @Override
  public String getTerm() {
    return TERM;
  }

}
