package eu.dissco.core.translator.terms.specimen.identification;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class IdentificationVerificationStatus extends Term {

  public static final String TERM = DWC_PREFIX + "identificationVerificationStatus";

  private final List<String> dwcaTerms = List.of(TERM);

  private final List<String> abcdTerms = List.of("preferredFlag");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var result = super.searchJsonForTerm(unit, dwcaTerms);
    if (result.equals("1")) {
      return Boolean.TRUE.toString();
    } else {
      return Boolean.FALSE.toString();
    }
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
