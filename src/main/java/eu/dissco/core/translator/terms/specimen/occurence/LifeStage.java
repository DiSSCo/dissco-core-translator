package eu.dissco.core.translator.terms.specimen.occurence;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class LifeStage extends Term {

  public static final String TERM = DWC_PREFIX + "lifeStage";

  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of("abcd:mycologicalUnit/mycologicalSexualStage",
      "mycologicalUnit/mycologicalLiveStages/0/mycologicalLiveStage",
      "zoologicalUnit/phasesOrStages/0/phaseOrStage");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
