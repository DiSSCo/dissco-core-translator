package eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class LatestEraOrHighestErathem extends AbstractChronoStratigraphy {

  public static final String TERM = DWC_PREFIX + "latestEraOrHighestErathem";
  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchABCDSplitTerms(unit, List.of("Erathem"));
  }

  @Override
  public String getTerm() {
    return TERM;
  }


}
