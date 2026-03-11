package eu.dissco.core.translator.terms.media;

import eu.dissco.core.translator.terms.Term;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;

@Slf4j
public class Language extends Term {

  public static final String TERM = "dcterms:language";
  private final List<String> dwcaTerms = List.of(TERM, "dc:language");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
