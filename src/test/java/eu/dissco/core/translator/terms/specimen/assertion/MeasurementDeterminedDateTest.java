package eu.dissco.core.translator.terms.specimen.assertion;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeasurementDeterminedDateTest {

  private final MeasurementDeterminedDate measurementDeterminedDate = new MeasurementDeterminedDate();

  private final String measurementDeterminedDateString = "09-10-2023T12:00:00Z";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:measurementDeterminedDate", measurementDeterminedDateString);

    // When
    var result = measurementDeterminedDate.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(measurementDeterminedDateString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("measurementOrFactAtomised/measurementDateTime", measurementDeterminedDateString);

    // When
    var result = measurementDeterminedDate.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(measurementDeterminedDateString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = measurementDeterminedDate.getTerm();

    // Then
    assertThat(result).isEqualTo(MeasurementDeterminedDate.TERM);
  }
}
