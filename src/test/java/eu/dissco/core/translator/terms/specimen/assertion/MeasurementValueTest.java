package eu.dissco.core.translator.terms.specimen.assertion;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeasurementValueTest {

  private final MeasurementValue measurementValue = new MeasurementValue();

  private final String measurementValueString = "45";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:measurementValue", measurementValueString);

    // When
    var result = measurementValue.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(measurementValueString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("measurementOrFactAtomised/upperValue", measurementValueString);

    // When
    var result = measurementValue.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(measurementValueString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = measurementValue.getTerm();

    // Then
    assertThat(result).isEqualTo(MeasurementValue.TERM);
  }
}
