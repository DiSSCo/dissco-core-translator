package eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class EarliestEonOrLowestEonothem extends AbstractChronoStratigraphy {

  public static final String TERM = DWC_PREFIX + "earliestEonOrLowestEonothem";
  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchABCDSplitTerms(unit, List.of("Eonothem"));
  }

  @Override
  public String getTerm() {
    return TERM;
  }

}
