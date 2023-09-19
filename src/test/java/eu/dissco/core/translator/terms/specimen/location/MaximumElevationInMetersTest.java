package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaximumElevationInMetersTest {

  private static final String MAXIMUM_ELEVATION_IN_METERS_STRING = "350";
  private final MaximumElevationInMeters maximumElevationInMeters = new MaximumElevationInMeters();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:maximumElevationInMeters", MAXIMUM_ELEVATION_IN_METERS_STRING);

    // When
    var result = maximumElevationInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MAXIMUM_ELEVATION_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/altitude/measurementOrFactAtomised/upperValue/value",
        MAXIMUM_ELEVATION_IN_METERS_STRING);

    // When
    var result = maximumElevationInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MAXIMUM_ELEVATION_IN_METERS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = maximumElevationInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(MaximumElevationInMeters.TERM);
  }
}
