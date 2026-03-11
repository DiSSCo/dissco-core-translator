package eu.dissco.core.translator.terms.specimen.citation;

import eu.dissco.core.translator.terms.Term;
import java.util.List;
import tools.jackson.databind.JsonNode;

public class ReferenceIRI extends Term {

  public static final String TERM = "dcterms:identifier";

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
