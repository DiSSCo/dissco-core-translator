package eu.dissco.core.translator.terms.specimen.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.schema.Assertion;
import eu.dissco.core.translator.terms.Term;
import java.util.ArrayList;
import java.util.List;

public class EventAssertions extends Term {

  private static final String EXTENSIONS = "extensions";

  public List<Assertion> gatherEventAssertions(
      ObjectMapper mapper, JsonNode data, boolean dwc) {
    if (dwc) {
      return gatherEventAssertionsForDwc(data);
    } else {
      return gatherEventAssertionsForABCD(mapper, data);
    }
  }

  private List<Assertion> gatherEventAssertionsForABCD(
      ObjectMapper mapper, JsonNode data) {
    var assertions = new ArrayList<Assertion>();
    var iterateOverElements = true;
    var count = 0;
    while (iterateOverElements) {
      var assertionNode = getSubJsonAbcd(mapper, data, count,
          "abcd:measurementsOrFacts/measurementOrFact/");
      if (!assertionNode.isEmpty()) {
        assertions.add(createEventAssertion(assertionNode));
        count++;
      } else {
        iterateOverElements = false;
      }
    }
    return assertions;
  }

  private Assertion createEventAssertion(
      JsonNode assertionNode) {
    return new Assertion()
        .withType("ods:Assertion")
        .withOdsAssertionByAgentName(
            super.searchJsonForTerm(assertionNode, List.of("measurementOrFactAtomised/measuredBy")))
        .withDwcMeasurementUnit(super.searchJsonForTerm(assertionNode,
            List.of("measurementOrFactAtomised/unitOfMeasurement")))
        .withDwcMeasurementType(
            super.searchJsonForTerm(assertionNode,
                List.of("measurementOrFactAtomised/parameter/value")))
        .withOdsAssertionRemarks(
            super.searchJsonForTerm(assertionNode, List.of("measurementOrFactText/value")))
        .withDwcMeasurementValue(super.searchJsonForTerm(assertionNode,
            List.of("measurementOrFactAtomised/lowerValue",
                "measurementOrFactAtomised/upperValue")))
        .withDwcMeasurementDeterminedDate(super.searchJsonForTerm(assertionNode,
            List.of("measurementOrFactAtomised/MeasurementDateTime")));
  }

  private List<Assertion> gatherEventAssertionsForDwc(
      JsonNode data) {
    var assertions = new ArrayList<Assertion>();
    if (data.get(EXTENSIONS) != null
        && data.get(EXTENSIONS).get("dwc:MeasurementOrFact") != null) {
      var measurementOrFactExtension = data.get(EXTENSIONS).get("dwc:MeasurementOrFact");
      for (var jsonNode : measurementOrFactExtension) {
        var assertion = new Assertion()
            .withType("ods:Assertion")
            .withDwcMeasurementUnit(
                super.searchJsonForTerm(jsonNode, List.of("dwc:measurementUnit")))
            .withDwcMeasurementType(
                super.searchJsonForTerm(jsonNode, List.of("dwc:measurementType")))
            .withDwcMeasurementValue(
                super.searchJsonForTerm(jsonNode, List.of("dwc:measurementValue")))
            .withOdsAssertionRemarks(
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
