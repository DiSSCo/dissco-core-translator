package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MOCK_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventAssertionsTest {

  private static final String MEASURED_BY = "S. Leeflang";
  private static final String UNIT = "meters";
  private static final String TYPE = "full length";
  private static final String VALUE = "2.5";
  private static final String REMARK = "Measurement of the full length10";
  private final EventAssertions eventAssertions = new EventAssertions();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    var extensions = MAPPER.createObjectNode();
    var measurementOrFacts = MAPPER.createArrayNode();
    var measurementOrFact = MAPPER.createObjectNode();
    measurementOrFact.put("dwc:measurementUnit", UNIT);
    measurementOrFact.put("dwc:measurementType", TYPE);
    measurementOrFact.put("dwc:measurementValue", VALUE);
    measurementOrFact.put("dwc:measurementRemarks", REMARK);
    measurementOrFacts.add(measurementOrFact);
    extensions.set("dwc:MeasurementOrFact", measurementOrFacts);
    unit.set("extensions", extensions);

    var expected = new eu.dissco.core.translator.schema.Assertion()
        .withType("ods:Assertion")
        .withDwcMeasurementUnit(UNIT)
        .withDwcMeasurementType(TYPE)
        .withDwcMeasurementValue(VALUE)
        .withOdsAssertionRemarks(REMARK);
    // When
    var result = eventAssertions.gatherEventAssertions(MAPPER, unit, true);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(expected);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:measurementsOrFacts/measurementOrFact/0/measurementOrFactAtomised/measuredBy",
        MEASURED_BY);
    unit.put(
        "abcd:measurementsOrFacts/measurementOrFact/0/measurementOrFactAtomised/unitOfMeasurement",
        UNIT);
    unit.put(
        "abcd:measurementsOrFacts/measurementOrFact/0/measurementOrFactAtomised/parameter/value",
        TYPE);
    unit.put("abcd:measurementsOrFacts/measurementOrFact/0/measurementOrFactText/value", REMARK);
    unit.put("abcd:measurementsOrFacts/measurementOrFact/0/measurementOrFactAtomised/lowerValue",
        VALUE);
    unit.put(
        "abcd:measurementsOrFacts/measurementOrFact/0/measurementOrFactAtomised/MeasurementDateTime",
        MOCK_DATE);

    var expected = new eu.dissco.core.translator.schema.Assertion()
        .withType("ods:Assertion")
        .withDwcMeasurementUnit(UNIT)
        .withDwcMeasurementType(TYPE)
        .withDwcMeasurementValue(VALUE)
        .withOdsAssertionByAgentName(MEASURED_BY)
        .withDwcMeasurementDeterminedDate(MOCK_DATE)
        .withOdsAssertionRemarks(REMARK);
    // When
    var result = eventAssertions.gatherEventAssertions(MAPPER, unit, false);

    // Then
    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(expected);
  }
}
