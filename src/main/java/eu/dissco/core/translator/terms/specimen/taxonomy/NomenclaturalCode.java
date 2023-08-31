package eu.dissco.core.translator.terms.specimen.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class NomenclaturalCode extends AbstractTaxonomy {

  public static final String TERM = DWC_PREFIX + "nomenclaturalCode";

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
