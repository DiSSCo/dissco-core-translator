package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaximumDistanceAboveSurfaceInMetersTest {

  private static final String MAXIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING = "350";
  private final MaximumDistanceAboveSurfaceInMeters maximumDistanceAboveSurfaceInMeters = new MaximumDistanceAboveSurfaceInMeters();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:maximumDistanceAboveSurfaceInMeters",
        MAXIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING);

    // When
    var result = maximumDistanceAboveSurfaceInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MAXIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/height/measurementOrFactAtomised/upperValue/value",
        MAXIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING);

    // When
    var result = maximumDistanceAboveSurfaceInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MAXIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = maximumDistanceAboveSurfaceInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(MaximumDistanceAboveSurfaceInMeters.TERM);
  }
}
