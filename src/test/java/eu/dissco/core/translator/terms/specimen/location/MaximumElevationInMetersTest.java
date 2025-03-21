package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaximumElevationInMetersTest {

  private static final String MAXIMUM_ELEVATION_IN_METERS_STRING = "350";
  private final MaximumElevationInMeters maximumElevationInMeters = new MaximumElevationInMeters();

  @ParameterizedTest
  @ValueSource(strings = {"350m.", "350meter", "350 m", "350 MTR"})
  void testRetrieveFromDWCA(String input) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:maximumElevationInMeters", input);

    // When
    var result = maximumElevationInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MAXIMUM_ELEVATION_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromDWCANull() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("dwc:maximumElevationInMeters");

    // When
    var result = maximumElevationInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isNull();
  }

  @ParameterizedTest
  @ValueSource(strings = {"350m.", "350meter", "350 m", "350 MTR"})
  void testRetrieveFromABCD(String input) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/altitude/measurementOrFactAtomised/upperValue/value", input);

    // When
    var result = maximumElevationInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MAXIMUM_ELEVATION_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromABCDNull() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("abcd:gathering/altitude/measurementOrFactAtomised/upperValue/value");

    // When
    var result = maximumElevationInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = maximumElevationInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(MaximumElevationInMeters.TERM);
  }
}
