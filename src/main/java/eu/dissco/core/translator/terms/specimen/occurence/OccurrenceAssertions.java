package eu.dissco.core.translator.terms.specimen.occurence;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.schema.Assertions;
import eu.dissco.core.translator.terms.Term;
import java.util.ArrayList;
import java.util.List;

public class OccurrenceAssertions extends Term {

  public List<eu.dissco.core.translator.schema.Assertions> gatherOccurrenceAssertions(JsonNode data,
      boolean dwc) {
    if (dwc) {
      return gatherOccurrenceAssertionsForDwc(data);
    } else {
      return gatherOccurrenceAssertionsForABCD(data);
    }
  }

  private List<eu.dissco.core.translator.schema.Assertions> gatherOccurrenceAssertionsForABCD(
      JsonNode data) {
    return List.of();
  }

  private List<eu.dissco.core.translator.schema.Assertions> gatherOccurrenceAssertionsForDwc(
      JsonNode data) {
    var assertions = new ArrayList<Assertions>();
    if (data.get("extensions") != null
        && data.get("extensions").get("dwc:MeasurementOrFact") != null) {
      var measurementOrFactExtension = data.get("extensions").get("dwc:MeasurementOrFact");
      for (var jsonNode : measurementOrFactExtension) {
        var assertion = new eu.dissco.core.translator.schema.Assertions()
            .withAssertionUnit(super.searchJsonForStringTerm(jsonNode, List.of("dwc:measurementUnit")))
            .withAssertionType(super.searchJsonForStringTerm(jsonNode, List.of("dwc:measurementType")))
            .withAssertionValue(
                super.searchJsonForStringTerm(jsonNode, List.of("dwc:measurementValue")));
        assertions.add(assertion);
      }
    }
    return assertions;
  }

  @Override
  public String getTerm() {
    return null;
  }
}
