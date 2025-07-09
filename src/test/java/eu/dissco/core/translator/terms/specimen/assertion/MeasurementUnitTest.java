package eu.dissco.core.translator.terms.specimen.assertion;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeasurementUnitTest {

  private final MeasurementUnit measurementUnit = new MeasurementUnit();

  private final String measurementUnitString = "g";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:measurementUnit", measurementUnitString);

    // When
    var result = measurementUnit.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(measurementUnitString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("measurementOrFactAtomised/unitOfMeasurement", measurementUnitString);

    // When
    var result = measurementUnit.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(measurementUnitString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = measurementUnit.getTerm();

    // Then
    assertThat(result).isEqualTo(MeasurementUnit.TERM);
  }
}
