package eu.dissco.core.translator.terms.specimen.chronometric;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class VerbatimChronometricAge extends Term {

  public static final String TERM = CHRONO_PREFIX + "verbatimChronometricAge";

  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of("datingAccuracy");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var value = super.searchJsonForTerm(unit, abcdTerms);
    if (value != null) {
      return "ABCDEFG datingAccuracy is: " + value;
    } else {
      return null;
    }
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
