package eu.dissco.core.translator.terms.specimen.identification;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class IdentifiedBy extends Term {
  public static final String TERM = DWC_PREFIX + "identifiedBy";

  private final List<String> dwcaTerms = List.of(TERM);

  private final List<String> abcdTerms = List.of("identifiers/identifier/0/personName/fullName");

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
