package eu.dissco.core.translator.terms.specimen.occurence;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class OrganismQuantity extends Term {
  public static final String TERM = DWC_PREFIX + "organismQuantity";

  private final List<String> dwcaTerms = List.of(TERM, "dwc:individualCount");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
