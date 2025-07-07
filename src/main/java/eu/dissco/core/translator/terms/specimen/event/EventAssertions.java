package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.domain.AgentRoleType.MEASURER;
import static eu.dissco.core.translator.schema.Agent.Type.SCHEMA_PERSON;
import static eu.dissco.core.translator.terms.utils.AgentsUtils.addAgent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.schema.Agent;
import eu.dissco.core.translator.schema.Agent.Type;
import eu.dissco.core.translator.schema.Assertion;
import eu.dissco.core.translator.schema.OdsHasRole;
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
        .withOdsHasAgents(
            List.of(new Agent()
                .withType(Type.SCHEMA_PERSON)
                .withSchemaName(super.searchJsonForTerm(assertionNode,
                    List.of("measurementOrFactAtomised/measuredBy")))
                .withOdsHasRoles(List.of(new OdsHasRole().withSchemaRoleName("measurer"))))
        )
        .withDwcMeasurementUnit(super.searchJsonForTerm(assertionNode,
            List.of("measurementOrFactAtomised/unitOfMeasurement")))
        .withDwcMeasurementType(
            super.searchJsonForTerm(assertionNode,
                List.of("measurementOrFactAtomised/parameter/value")))
        .withDwcMeasurementRemarks(
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
    if (data.get(EXTENSIONS) != null) {
      if (data.get(EXTENSIONS).get("dwc:MeasurementOrFact") != null) {
        var measurementOrFact = data.get(EXTENSIONS).get("dwc:MeasurementOrFact");
        mapMeasurementOrFact(measurementOrFact, assertions);
      }
      if (data.get(EXTENSIONS).get("http://rs.iobis.org/obis/terms/ExtendedMeasurementOrFact")
          != null) {
        var extendedMeasurementOrFact = data.get(EXTENSIONS)
            .get("http://rs.iobis.org/obis/terms/ExtendedMeasurementOrFact");
        mapMeasurementOrFact(extendedMeasurementOrFact, assertions);
      }
    }
    return assertions;
  }

  private void mapMeasurementOrFact(JsonNode measurementOrFactExtension,
      List<Assertion> assertions) {
    for (var jsonNode : measurementOrFactExtension) {
      var assertion = new Assertion()
          .withType("ods:Assertion")
          .withDwcMeasurementID(super.searchJsonForTerm(jsonNode, List.of("dwc:measurementID")))
          .withDwcParentMeasurementID(
              super.searchJsonForTerm(jsonNode, List.of("dwc:parentMeasurementID")))
          .withDwcMeasurementUnit(
              super.searchJsonForTerm(jsonNode, List.of("dwc:measurementUnit")))
          .withDwciriMeasurementUnit(
              super.searchJsonForTerm(jsonNode,
                  List.of("http://rs.iobis.org/obis/terms/measurementUnitID")))
          .withDwcMeasurementType(
              super.searchJsonForTerm(jsonNode, List.of("dwc:measurementType")))
          .withDwciriMeasurementType(
              super.searchJsonForTerm(jsonNode,
                  List.of("http://rs.iobis.org/obis/terms/measurementTypeID")))
          .withDwcMeasurementValue(
              super.searchJsonForTerm(jsonNode, List.of("dwc:measurementValue")))
          .withDwciriMeasurementValue(
              super.searchJsonForTerm(jsonNode,
                  List.of("http://rs.iobis.org/obis/terms/measurementValueID")))
          .withDwcMeasurementAccuracy(
              super.searchJsonForTerm(jsonNode, List.of("dwc:measurementAccuracy")))
          .withDwcMeasurementDeterminedDate(
              super.searchJsonForTerm(jsonNode, List.of("dwc:measurementDeterminedDate")))
          .withDwcMeasurementMethod(
              super.searchJsonForTerm(jsonNode, List.of("dwc:measurementMethod")))
          .withDwcMeasurementRemarks(
              super.searchJsonForTerm(jsonNode, List.of("dwc:measurementRemarks")));
      assertion.setOdsHasAgents(addAgent(assertion.getOdsHasAgents(),
          super.searchJsonForTerm(jsonNode, List.of("dwc:measurementDeterminedBy")), null, MEASURER,
          SCHEMA_PERSON));
      assertions.add(assertion);
    }
  }

  @Override
  public String getTerm() {
    return null;
  }
}
