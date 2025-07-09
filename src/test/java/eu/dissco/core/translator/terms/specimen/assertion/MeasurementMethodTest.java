package eu.dissco.core.translator.terms.specimen.assertion;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeasurementMethodTest {

  private final MeasurementMethod measurementMethod = new MeasurementMethod();

  private final String measurementMethodString = "barometric altimeter";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:measurementMethod", measurementMethodString);

    // When
    var result = measurementMethod.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(measurementMethodString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = measurementMethod.getTerm();

    // Then
    assertThat(result).isEqualTo(MeasurementMethod.TERM);
  }
}
