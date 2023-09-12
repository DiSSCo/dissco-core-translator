package eu.dissco.core.translator.terms.specimen.identification;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class VerbatimIdentification extends Term {

  public static final String TERM = DWC_PREFIX + "verbatimIdentification";

  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }


  @Override
  public String getTerm() {
    return TERM;
  }
}
