package eu.dissco.core.translator.terms.specimen.occurence;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class OrganismQuantityType extends Term {

  public static final String TERM = DWC_PREFIX + "organismQuantityType";

  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var type = super.searchJsonForTerm(unit, dwcaTerms);
    if (type == null) {
      var individuals = super.searchJsonForTerm(unit, List.of("dwc:individualCount"));
      if (individuals != null) {
        return "individuals";
      }
    }
    return type;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
