package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class InfraGenericEpithet extends Term {

  public static final String TERM = DWC_PREFIX + "infragenericEpithet";

  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdTerms = List.of(
      "result/taxonIdentified/scientificName/nameAtomised/botanical/firstEpithet",
      "result/taxonIdentified/scientificName/nameAtomised/zoological/subgenus",
      "result/taxonIdentified/scientificName/nameAtomised/bacterial/subgenus");

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
