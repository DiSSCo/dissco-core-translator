package eu.dissco.core.translator.terms.specimen.chronometric;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class ChronometricAgeRemarks extends Term {

  public static final String TERM = "chrono:chronometricAgeRemarks";

  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of("datingComment");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchJsonForTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
