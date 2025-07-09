package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MOCK_DATE;
import static eu.dissco.core.translator.TestUtils.SOME_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import eu.dissco.core.translator.terms.TermMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventAssertionsTest {

  private static final String MEASURED_BY = "S. Leeflang";
  private static final String UNIT = "meters";
  private static final String TYPE = "full length";
  private static final String VALUE = "2.5";
  private static final String REMARK = "Measurement of the full length10";
  private final EventAssertions eventAssertions = new EventAssertions();
  @Mock
  private TermMapper termMapper;

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
    given(termMapper.retrieveTerm(any(Term.class), eq(measurementOrFact), eq(true))).willReturn(
        SOME_VALUE);

    // When
    var result = eventAssertions.gatherEventAssertions(termMapper, MAPPER, unit, true);

    // Then
    assertThat(result).hasSize(1);
  }

  @Test
  void testRetrieveFromExtendedDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    var extensions = MAPPER.createObjectNode();
    var measurementOrFacts = MAPPER.createArrayNode();
    var measurementOrFact = MAPPER.createObjectNode();
    measurementOrFact.put("dwc:measurementUnit", "sex");
    measurementOrFact.put("http://rs.iobis.org/obis/terms/measurementUnitID",
        "vocab.nerc.ac.uk/collection/P01/current/ENTSEX01");
    measurementOrFact.put("dwc:measurementValue", "female");
    measurementOrFact.put("http://rs.iobis.org/obis/terms/measurementValueID",
        "http://vocab.nerc.ac.uk/collection/S10/current/S102");
    measurementOrFacts.add(measurementOrFact);
    extensions.set("http://rs.iobis.org/obis/terms/ExtendedMeasurementOrFact", measurementOrFacts);
    unit.set("extensions", extensions);
    given(termMapper.retrieveTerm(any(Term.class), eq(measurementOrFact), eq(true))).willReturn(
        SOME_VALUE);

    // When
    var result = eventAssertions.gatherEventAssertions(termMapper, MAPPER, unit, true);

    // Then
    assertThat(result).hasSize(1);
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
    given(termMapper.retrieveTerm(any(Term.class), any(JsonNode.class), eq(false))).willReturn(
        SOME_VALUE);

    // When
    var result = eventAssertions.gatherEventAssertions(termMapper, MAPPER, unit, false);

    // Then
    assertThat(result).hasSize(1);
  }
}
