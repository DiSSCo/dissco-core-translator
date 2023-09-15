package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpecimenName extends AbstractTaxonomy {

  public static final String TERM = ODS_PREFIX + "specimenName";
  private final List<String> dwcaTerms = List.of("dwc:scientificName");
  private final List<String> abcdTerms = List.of(
      "result/taxonIdentified/scientificName/fullScientificNameString",
      "result/mineralRockIdentified/classifiedName/fullScientificNameString");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }


  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return searchJsonForTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
