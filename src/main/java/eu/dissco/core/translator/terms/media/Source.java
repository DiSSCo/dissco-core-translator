package eu.dissco.core.translator.terms.media;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class Source extends Term {

  public static final String TERM = "dcterms:source";
  private final List<String> dwcaTerms = List.of(TERM, "dc:source");
  private final List<String> abcdTerms = List.of("abcd:productURI");

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
