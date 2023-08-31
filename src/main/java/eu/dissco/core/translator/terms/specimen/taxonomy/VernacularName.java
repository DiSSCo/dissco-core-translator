package eu.dissco.core.translator.terms.specimen.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class VernacularName extends AbstractTaxonomy{

  public static final String TERM = DWC_PREFIX + "vernacularName";

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
