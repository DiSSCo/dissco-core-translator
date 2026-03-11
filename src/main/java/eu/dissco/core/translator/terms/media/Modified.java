package eu.dissco.core.translator.terms.media;

import eu.dissco.core.translator.terms.Term;
import java.util.List;
import tools.jackson.databind.JsonNode;

public class Modified extends Term {

  public static final String TERM = "dcterms:modified";
  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
