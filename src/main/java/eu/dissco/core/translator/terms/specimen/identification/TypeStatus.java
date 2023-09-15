package eu.dissco.core.translator.terms.specimen.identification;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.specimen.identification.taxonomy.AbstractTaxonomy;
import java.util.List;

public class TypeStatus extends AbstractTaxonomy {

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
    return super.searchJsonForTerm(unit, dwcaTerms);
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
