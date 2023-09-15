package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class Modified extends Term {

  public static final String TERM = "dcterms:modified";
  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdUnitTerms = List.of("abcd:hasDateModified", "abcd:dateLastEdited",
      "abcd:revisionData/dateModified");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return searchJsonForTerm(unit, abcdUnitTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
