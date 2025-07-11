package eu.dissco.core.translator.terms.specimen.assertion;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeasurementIDTest {

  private final MeasurementID measurementID = new MeasurementID();

  private final String measurementIDString = "9c752d22-b09a-11e8-96f8-529269fb1459";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:measurementID", measurementIDString);

    // When
    var result = measurementID.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(measurementIDString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = measurementID.getTerm();

    // Then
    assertThat(result).isEqualTo(MeasurementID.TERM);
  }
}
