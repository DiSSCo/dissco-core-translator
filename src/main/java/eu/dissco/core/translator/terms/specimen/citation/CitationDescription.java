package eu.dissco.core.translator.terms.specimen.citation;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class CitationDescription extends Term {

  public static final String TERM = "dcterms:description";

  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of("citationDetail");

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
