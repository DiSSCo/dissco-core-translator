package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class NameAccordingTo extends AbstractTaxonomy {

  public static final String TERM = DWC_PREFIX + "nameAccordingTo";

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
