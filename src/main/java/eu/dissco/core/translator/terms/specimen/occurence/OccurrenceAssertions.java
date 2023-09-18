package eu.dissco.core.translator.terms.specimen.occurence;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.schema.Assertions;
import eu.dissco.core.translator.terms.Term;
import java.util.ArrayList;
import java.util.List;

public class OccurrenceAssertions extends Term {

  private static final String EXTENSIONS = "extensions";

  public List<eu.dissco.core.translator.schema.Assertions> gatherOccurrenceAssertions(
      ObjectMapper mapper, JsonNode data, boolean dwc) {
    if (dwc) {
      return gatherOccurrenceAssertionsForDwc(data);
    } else {
      return gatherOccurrenceAssertionsForABCD(mapper, data);
    }
  }

  private List<eu.dissco.core.translator.schema.Assertions> gatherOccurrenceAssertionsForABCD(
      ObjectMapper mapper,
      JsonNode data) {
    var assertions = new ArrayList<Assertions>();
    var iterateOverElements = true;
    var count = 0;
    while (iterateOverElements) {
      var assertionNode = getSubJsonAbcd(mapper, data, count,
          "abcd:measurementsOrFacts/measurementOrFact");
      if (!assertionNode.isEmpty()) {
        assertions.add(createOccurrenceAssertion(assertionNode));
        count++;
      } else {
        iterateOverElements = false;
      }
    }
    return assertions;
  }

  private eu.dissco.core.translator.schema.Assertions createOccurrenceAssertion(
      JsonNode assertionNode) {
    return new Assertions().withAssertionByAgentName(
            super.searchJsonForTerm(assertionNode, List.of("measurementOrFactAtomised/measuredBy")))
        .withAssertionUnit(super.searchJsonForTerm(assertionNode,
            List.of("measurementOrFactAtomised/unitOfMeasurement")))
        .withAssertionType(
            super.searchJsonForTerm(assertionNode, List.of("measurementOrFactAtomised/parameter")))
        .withAssertionByAgentName(super.searchJsonForTerm(assertionNode, List.of("/measuredBy")))
        .withAssertionRemarks(
            super.searchJsonForTerm(assertionNode, List.of("measurementOrFactText/value")))
        .withAssertionValue(super.searchJsonForTerm(assertionNode,
            List.of("measurementOrFactAtomised/lowerValue",
                "measurementOrFactAtomised/upperValue")))
        .withAssertionMadeDate(super.searchJsonForTerm(assertionNode,
            List.of("measurementOrFactAtomised/MeasurementDateTime")));
  }

  private List<eu.dissco.core.translator.schema.Assertions> gatherOccurrenceAssertionsForDwc(
      JsonNode data) {
    var assertions = new ArrayList<Assertions>();
    if (data.get(EXTENSIONS) != null
        && data.get(EXTENSIONS).get("dwc:MeasurementOrFact") != null) {
      var measurementOrFactExtension = data.get(EXTENSIONS).get("dwc:MeasurementOrFact");
      for (var jsonNode : measurementOrFactExtension) {
        var assertion = new eu.dissco.core.translator.schema.Assertions()
            .withAssertionUnit(
                super.searchJsonForTerm(jsonNode, List.of("dwc:measurementUnit")))
            .withAssertionType(
                super.searchJsonForTerm(jsonNode, List.of("dwc:measurementType")))
            .withAssertionValue(
                super.searchJsonForTerm(jsonNode, List.of("dwc:measurementValue")))
            .withAssertionRemarks(
                super.searchJsonForTerm(jsonNode, List.of("dwc:measurementRemarks")));
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
