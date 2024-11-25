package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MinimumElevationInMetersTest {

  private static final String MINIMUM_ELEVATION_IN_METERS_STRING = "250";
  private final MinimumElevationInMeters minimumElevationInMeters = new MinimumElevationInMeters();

  @ParameterizedTest
  @ValueSource(strings = {"250m.  ", "250 meter", "250 m", "250 MTR"})
  void testRetrieveFromDWCA(String input) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:minimumElevationInMeters", input);

    // When
    var result = minimumElevationInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MINIMUM_ELEVATION_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromDWCANull() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("dwc:minimumElevationInMeters");

    // When
    var result = minimumElevationInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isNull();
  }

  @ParameterizedTest
  @ValueSource(strings = {"250m.  ", "250 meter", "250 m", "250 MTR"})
  void testRetrieveFromABCD(String input) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/altitude/measurementOrFactAtomised/lowerValue/value", input);

    // When
    var result = minimumElevationInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MINIMUM_ELEVATION_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromABCDNull() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("abcd:gathering/altitude/measurementOrFactAtomised/lowerValue/value");

    // When
    var result = minimumElevationInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = minimumElevationInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(MinimumElevationInMeters.TERM);
  }
}
