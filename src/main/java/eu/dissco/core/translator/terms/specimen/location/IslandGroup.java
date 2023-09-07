package eu.dissco.core.translator.terms.specimen.location;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class IslandGroup extends Term {

  public static final String TERM = DWC_PREFIX + "islandGroup";
  private final List<String> dwcaTerms = List.of(TERM);

  private final List<String> abcdTerms = List.of("Island group");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchABCDSplitTerms(unit, abcdTerms, ABCD_NAMED_AREA_KEY, ABCD_NAMED_AREA_VALUE);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
