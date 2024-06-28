package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class PhysicalSpecimenID extends Term {

  public static final String TERM = ODS_PREFIX + "physicalSpecimenID";

  private final List<String> dwcaTerms = List.of("dwc:occurrenceID", "dwc:catalogNumber",
      "dwc:materialSampleID", "dwc:materialEntityID");
  private final List<String> abcdTerms = List.of("abcd:unitGUID", "abcd:unitID");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchJsonForTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
