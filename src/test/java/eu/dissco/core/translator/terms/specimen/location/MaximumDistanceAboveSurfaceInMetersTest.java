package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaximumDistanceAboveSurfaceInMetersTest {

  private static final String MAXIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING = "350";
  private final MaximumDistanceAboveSurfaceInMeters maximumDistanceAboveSurfaceInMeters = new MaximumDistanceAboveSurfaceInMeters();

  @ParameterizedTest
  @ValueSource(strings = {"350m.", "350meter", "350 m m", "350 mtr"})
  void testRetrieveFromDWCA(String input) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:maximumDistanceAboveSurfaceInMeters", input);

    // When
    var result = maximumDistanceAboveSurfaceInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MAXIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromDWCANull() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("dwc:maximumDistanceAboveSurfaceInMeters");

    // When
    var result = maximumDistanceAboveSurfaceInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isNull();
  }

  @ParameterizedTest
  @ValueSource(strings = {"350m.", "350meter", "350 m m", "350 mtr"})
  void testRetrieveFromABCD(String input) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/height/measurementOrFactAtomised/upperValue/value", input);

    // When
    var result = maximumDistanceAboveSurfaceInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MAXIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromABCDNull() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("abcd:gathering/height/measurementOrFactAtomised/upperValue/value");

    // When
    var result = maximumDistanceAboveSurfaceInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = maximumDistanceAboveSurfaceInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(MaximumDistanceAboveSurfaceInMeters.TERM);
  }
}
