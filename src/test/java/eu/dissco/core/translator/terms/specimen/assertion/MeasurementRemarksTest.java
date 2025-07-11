package eu.dissco.core.translator.terms.specimen.assertion;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeasurementRemarksTest {

  private final MeasurementRemarks measurementRemarks = new MeasurementRemarks();

  private final String measurementRemarksString = "tip of tail missing";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:measurementRemarks", measurementRemarksString);

    // When
    var result = measurementRemarks.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(measurementRemarksString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("measurementOrFactText/value", measurementRemarksString);

    // When
    var result = measurementRemarks.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(measurementRemarksString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = measurementRemarks.getTerm();

    // Then
    assertThat(result).isEqualTo(MeasurementRemarks.TERM);
  }
}
