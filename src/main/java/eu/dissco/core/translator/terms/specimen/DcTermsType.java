package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class DcTermsType extends Term {
  public static final String TERM = "dcterms:type";

  private final List<String> dwcaTerms = List.of(TERM);
//  private final List<String> abcdTerms = List.of("abcd:recordBasis");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

//  @Override
//  public String retrieveFromABCD(JsonNode unit) {
//    return super.searchJsonForStringTerm(unit, abcdTerms);
//  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
