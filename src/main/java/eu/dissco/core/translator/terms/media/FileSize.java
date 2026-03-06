package eu.dissco.core.translator.terms.media;

import eu.dissco.core.translator.terms.Term;
import java.util.List;
import tools.jackson.databind.JsonNode;

public class FileSize extends Term {

  public static final String TERM = "dcterms:extent";
  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of("abcd:fileSize");

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
