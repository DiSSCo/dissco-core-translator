package eu.dissco.core.translator.terms.media;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class AccessURI extends Term {

  public static final String TERM = "ac:accessURI";
  public static final List<String> DWCA_TERMS = List.of(TERM, "dcterms:identifier", "dc:identifier", "dcterms:references");
  private final List<String> abcdTerms = List.of("abcd:fileURI");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, DWCA_TERMS);
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
