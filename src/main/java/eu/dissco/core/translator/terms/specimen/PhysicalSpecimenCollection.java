package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class PhysicalSpecimenCollection extends Term {

  public static final String TERM = ODS_PREFIX + "physicalSpecimenCollection";

  private final List<String> dwcaTerms = List.of("dwc:collectionID", "dwc:collectionCode");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }
  @Override
  public eu.dissco.core.translator.schema.DigitalSpecimen retrieveFromDWCANew(
      eu.dissco.core.translator.schema.DigitalSpecimen ds, JsonNode unit) {
    return ds.withDwcCollectionId(super.searchJsonForStringTerm(unit, dwcaTerms));
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
