package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class NomenclaturalCode extends AbstractTaxonomy {

  public static final String TERM = DWC_PREFIX + "nomenclaturalCode";

  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of("result/taxonIdentified/code");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return searchJsonForStringTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
