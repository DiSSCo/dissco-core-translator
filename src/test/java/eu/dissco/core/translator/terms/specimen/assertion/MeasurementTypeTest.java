package eu.dissco.core.translator.terms.specimen.assertion;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeasurementTypeTest {

  private final MeasurementType measurementType = new MeasurementType();

  private final String measurementTypeString = "temperature";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:measurementType", measurementTypeString);

    // When
    var result = measurementType.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(measurementTypeString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("measurementOrFactAtomised/parameter/value", measurementTypeString);

    // When
    var result = measurementType.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(measurementTypeString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = measurementType.getTerm();

    // Then
    assertThat(result).isEqualTo(MeasurementType.TERM);
  }
}
