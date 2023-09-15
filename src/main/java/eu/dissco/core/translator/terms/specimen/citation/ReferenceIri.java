package eu.dissco.core.translator.terms.specimen.citation;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class ReferenceIri extends Term {

  public static final String TERM = "???:referenceIri";

  private final List<String> abcdTerms = List.of("uri");

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchJsonForTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }


}
