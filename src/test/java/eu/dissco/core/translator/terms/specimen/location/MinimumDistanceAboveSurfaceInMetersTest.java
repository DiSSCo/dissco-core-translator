package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MinimumDistanceAboveSurfaceInMetersTest {

  private static final String MINIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING = "250";
  private final MinimumDistanceAboveSurfaceInMeters minimumDistanceAboveSurfaceInMeters = new MinimumDistanceAboveSurfaceInMeters();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:minimumDistanceAboveSurfaceInMeters", MINIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING);

    // When
    var result = minimumDistanceAboveSurfaceInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MINIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/height/measurementOrFactAtomised/lowerValue/value",
        MINIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING);

    // When
    var result = minimumDistanceAboveSurfaceInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MINIMUM_DISTANCE_ABOVE_SURFACE_IN_METERS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = minimumDistanceAboveSurfaceInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(MinimumDistanceAboveSurfaceInMeters.TERM);
  }
}
