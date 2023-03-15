package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class TypeStatus extends Term {

  public static final String TERM = DWC_PREFIX + "typeStatus";

  private final List<String> dwcaTerms = List.of(TERM);

  // Pick the first TypeStatus from ABCD
  private final List<String> abcdTerms =
      List.of(
          "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typeStatus",
          "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typifiedName/fullScientificNameString",
          "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/nomenclaturalReference/titleCitation"
      );

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var result = super.searchJsonForTerm(unit, dwcaTerms);
    if (result != null) {
      return result;
    } else {
      var extensions = unit.get("extensions");
      if (extensions != null) {
        var identification = extensions.get("dwc:Identification");
        if (identification.size() == 1) {
          return searchJsonForTerm(identification.get(0), dwcaTerms);
        }
      }
    }
    return null;
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.combineABCDTerms(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
