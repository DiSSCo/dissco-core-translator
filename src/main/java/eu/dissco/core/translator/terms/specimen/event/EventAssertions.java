package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.domain.AgentRoleType.MEASURER;
import static eu.dissco.core.translator.schema.Agent.Type.SCHEMA_PERSON;
import static eu.dissco.core.translator.terms.utils.AgentsUtils.addAgent;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.schema.Assertion;
import eu.dissco.core.translator.terms.Term;
import eu.dissco.core.translator.terms.TermMapper;
import eu.dissco.core.translator.terms.specimen.assertion.MeasurementAccuracy;
import eu.dissco.core.translator.terms.specimen.assertion.MeasurementDeterminedDate;
import eu.dissco.core.translator.terms.specimen.assertion.MeasurementID;
import eu.dissco.core.translator.terms.specimen.assertion.MeasurementMethod;
import eu.dissco.core.translator.terms.specimen.assertion.MeasurementRemarks;
import eu.dissco.core.translator.terms.specimen.assertion.MeasurementType;
import eu.dissco.core.translator.terms.specimen.assertion.MeasurementTypeIRI;
import eu.dissco.core.translator.terms.specimen.assertion.MeasurementUnit;
import eu.dissco.core.translator.terms.specimen.assertion.MeasurementUnitIRI;
import eu.dissco.core.translator.terms.specimen.assertion.MeasurementValue;
import eu.dissco.core.translator.terms.specimen.assertion.MeasurementValueIRI;
import eu.dissco.core.translator.terms.specimen.assertion.ParentMeasurementID;
import java.util.ArrayList;
import java.util.List;

public class EventAssertions extends Term {

  private static final String EXTENSIONS = "extensions";

  public List<Assertion> gatherEventAssertions(
      TermMapper termMapper, ObjectMapper mapper, JsonNode data, boolean dwc) {
    if (dwc) {
      return gatherEventAssertionsForDwc(termMapper, data);
    } else {
      return gatherEventAssertionsForABCD(termMapper, mapper, data);
    }
  }

  private List<Assertion> gatherEventAssertionsForABCD(
      TermMapper termMapper, ObjectMapper mapper, JsonNode data) {
    var assertions = new ArrayList<Assertion>();
    var iterateOverElements = true;
    var count = 0;
    while (iterateOverElements) {
      var assertionNode = getSubJsonAbcd(mapper, data, count,
          "abcd:measurementsOrFacts/measurementOrFact/");
      if (!assertionNode.isEmpty()) {
        assertions.add(
            mapAssertion(termMapper, assertionNode, false, "measurementOrFactAtomised/measuredBy"));
        count++;
      } else {
        iterateOverElements = false;
      }
    }
    return assertions;
  }

  private List<Assertion> gatherEventAssertionsForDwc(
      TermMapper termMapper, JsonNode data) {
    var assertions = new ArrayList<Assertion>();
    if (data.get(EXTENSIONS) != null) {
      if (data.get(EXTENSIONS).get("dwc:MeasurementOrFact") != null) {
        var measurementOrFact = data.get(EXTENSIONS).get("dwc:MeasurementOrFact");
        mapMeasurementOrFact(termMapper, measurementOrFact, assertions);
      }
      if (data.get(EXTENSIONS).get("http://rs.iobis.org/obis/terms/ExtendedMeasurementOrFact")
          != null) {
        var extendedMeasurementOrFact = data.get(EXTENSIONS)
            .get("http://rs.iobis.org/obis/terms/ExtendedMeasurementOrFact");
        mapMeasurementOrFact(termMapper, extendedMeasurementOrFact, assertions);
      }
    }
    return assertions;
  }

  private void mapMeasurementOrFact(TermMapper termMapper, JsonNode measurementOrFactExtension,
      List<Assertion> assertions) {
    for (var data : measurementOrFactExtension) {
      assertions.add(mapAssertion(termMapper, data, true, "dwc:measurementDeterminedBy"));
    }
  }

  private Assertion mapAssertion(TermMapper termMapper, JsonNode data, boolean dwc,
      String agentNameField) {
    var assertion = new Assertion()
        .withId(termMapper.retrieveTerm(new MeasurementID(), data, dwc))
        .withType("ods:Assertion")
        .withDwcMeasurementID(termMapper.retrieveTerm(new MeasurementID(), data, dwc))
        .withDwcParentMeasurementID(termMapper.retrieveTerm(new ParentMeasurementID(), data
            , dwc))
        .withDwcMeasurementUnit(termMapper.retrieveTerm(new MeasurementUnit(), data, dwc))
        .withDwciriMeasurementUnit(termMapper.retrieveTerm(new MeasurementUnitIRI(), data, dwc))
        .withDwcMeasurementType(termMapper.retrieveTerm(new MeasurementType(), data, dwc))
        .withDwciriMeasurementType(termMapper.retrieveTerm(new MeasurementTypeIRI(), data, dwc))
        .withDwcMeasurementValue(termMapper.retrieveTerm(new MeasurementValue(), data, dwc))
        .withDwciriMeasurementValue(termMapper.retrieveTerm(new MeasurementValueIRI(), data, dwc))
        .withDwcMeasurementAccuracy(termMapper.retrieveTerm(new MeasurementAccuracy(), data, dwc))
        .withDwcMeasurementDeterminedDate(
            termMapper.retrieveTerm(new MeasurementDeterminedDate(), data, dwc))
        .withDwcMeasurementMethod(termMapper.retrieveTerm(new MeasurementMethod(), data, dwc))
        .withDwcMeasurementRemarks(termMapper.retrieveTerm(new MeasurementRemarks(), data, dwc));
    assertion.setOdsHasAgents(addAgent(assertion.getOdsHasAgents(),
        super.searchJsonForTerm(data, List.of(agentNameField)), null, MEASURER,
        SCHEMA_PERSON));
    return assertion;
  }

  @Override
  public String getTerm() {
    return null;
  }
}
