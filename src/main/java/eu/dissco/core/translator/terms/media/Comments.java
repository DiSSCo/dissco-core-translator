package eu.dissco.core.translator.terms.media;

import eu.dissco.core.translator.terms.Term;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;

@Slf4j
public class Comments extends Term {

  public static final String TERM = "ac:comments";
  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of("abcd:comment/value");

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
