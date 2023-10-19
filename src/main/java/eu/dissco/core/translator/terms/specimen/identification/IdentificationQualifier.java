package eu.dissco.core.translator.terms.specimen.identification;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class IdentificationQualifier extends Term {

  public static final String TERM = DWC_PREFIX + "identificationQualifier";

  private final List<String> dwcaTerms = List.of(TERM);

  private final List<String> abcdTerms = List.of(
      "result/taxonIdentified/scientificName/identificationQualifier/nameAddendum");

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
