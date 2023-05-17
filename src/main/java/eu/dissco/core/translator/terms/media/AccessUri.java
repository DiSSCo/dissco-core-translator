package eu.dissco.core.translator.terms.media;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class AccessUri extends Term {

  public static final String TERM = "ac:accessURI";
  private final List<String> dwcaTerms = List.of(TERM, "dcterms:identifier", "dc:identifier");
  private final List<String> abcdTerms = List.of("abcd:fileURI");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
