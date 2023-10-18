package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class BasisOfRecord extends Term {

  public static final String TERM = DWC_PREFIX + "basisOfRecord";
  private static final List<String> PRESERVED_SPECIMEN_ALTERNATIVES = List.of("HERBARIUM SHEET",
      "HERBARIUMSHEET", "DRIED");
  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of("abcd:recordBasis");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var result = super.searchJsonForTerm(unit, abcdTerms);
    if (result != null && PRESERVED_SPECIMEN_ALTERNATIVES.contains(result.toUpperCase())) {
      return "Preserved specimen";
    }
    return result;
  }

  @Override
  public String getTerm() {
    return TERM;
  }

}
