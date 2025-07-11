package eu.dissco.core.translator.terms.specimen.assertion;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeasurementAccuracyTest {

  private final MeasurementAccuracy measurementAccuracy = new MeasurementAccuracy();

  private final String measurementAccuracyString = "0.01";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:measurementAccuracy", measurementAccuracyString);

    // When
    var result = measurementAccuracy.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(measurementAccuracyString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("measurementOrFactAtomised/accuracy", measurementAccuracyString);

    // When
    var result = measurementAccuracy.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(measurementAccuracyString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = measurementAccuracy.getTerm();

    // Then
    assertThat(result).isEqualTo(MeasurementAccuracy.TERM);
  }
}
