package eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class EarliestAgeOrLowestStage extends AbstractChronoStratigraphy {

  public static final String TERM = DWC_PREFIX + "earliestAgeOrLowestStage";
  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchABCDSplitTerms(unit, List.of("SubStage", "Stage"));
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
