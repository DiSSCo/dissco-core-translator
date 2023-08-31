package eu.dissco.core.translator.terms.specimen.citation;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class BibliographicCitation extends Term {
  public static final String TERM = "dcterms:bibliographicCitation";

  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
