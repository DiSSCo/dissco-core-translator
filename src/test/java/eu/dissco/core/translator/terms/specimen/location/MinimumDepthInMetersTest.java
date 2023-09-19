package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MinimumDepthInMetersTest {

  private static final String MINIMUM_DEPTH_IN_METERS_STRING = "-50";
  private final MinimumDepthInMeters minimumDepthInMeters = new MinimumDepthInMeters();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:minimumDepthInMeters", MINIMUM_DEPTH_IN_METERS_STRING);

    // When
    var result = minimumDepthInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MINIMUM_DEPTH_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/depth/measurementOrFactAtomised/lowerValue/value",
        MINIMUM_DEPTH_IN_METERS_STRING);

    // When
    var result = minimumDepthInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MINIMUM_DEPTH_IN_METERS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = minimumDepthInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(MinimumDepthInMeters.TERM);
  }
}
