package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MinimumDistanceAboveSurfaceInMetersTest {

  private static final String MINIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING = "250";
  private final MinimumDistanceAboveSurfaceInMeters minimumDistanceAboveSurfaceInMeters = new MinimumDistanceAboveSurfaceInMeters();

  @ParameterizedTest
  @ValueSource(strings = {"250m.  ", "250 meter", "250 m", "250 MTR"})
  void testRetrieveFromDWCA(String input) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:minimumDistanceAboveSurfaceInMeters", input);

    // When
    var result = minimumDistanceAboveSurfaceInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MINIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromDWCANull() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("dwc:minimumDistanceAboveSurfaceInMeters");

    // When
    var result = minimumDistanceAboveSurfaceInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isNull();
  }

  @ParameterizedTest
  @ValueSource(strings = {"250m.  ", "250 meter", "250 m", "250 MTR"})
  void testRetrieveFromABCD(String input) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/height/measurementOrFactAtomised/lowerValue/value", input);

    // When
    var result = minimumDistanceAboveSurfaceInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MINIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromABCDNull() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("abcd:gathering/height/measurementOrFactAtomised/lowerValue/value");

    // When
    var result = minimumDistanceAboveSurfaceInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = minimumDistanceAboveSurfaceInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(MinimumDistanceAboveSurfaceInMeters.TERM);
  }
}
