package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MinimumElevationInMetersTest {

  private static final String MINIMUM_ELEVATION_IN_METERS_STRING = "250";
  private final MinimumElevationInMeters minimumElevationInMeters = new MinimumElevationInMeters();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:minimumElevationInMeters", MINIMUM_ELEVATION_IN_METERS_STRING);

    // When
    var result = minimumElevationInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MINIMUM_ELEVATION_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/altitude/measurementOrFactAtomised/lowerValue/value",
        MINIMUM_ELEVATION_IN_METERS_STRING);

    // When
    var result = minimumElevationInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MINIMUM_ELEVATION_IN_METERS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = minimumElevationInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(MinimumElevationInMeters.TERM);
  }
}
