package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaximumDepthInMetersTest {

  private static final String MAXIMUM_DEPTH_IN_METERS_STRING = "-350";
  private final MaximumDepthInMeters maximumDepthInMeters = new MaximumDepthInMeters();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:maximumDepthInMeters", MAXIMUM_DEPTH_IN_METERS_STRING);

    // When
    var result = maximumDepthInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MAXIMUM_DEPTH_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/depth/measurementOrFactAtomised/upperValue/value",
        MAXIMUM_DEPTH_IN_METERS_STRING);

    // When
    var result = maximumDepthInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MAXIMUM_DEPTH_IN_METERS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = maximumDepthInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(MaximumDepthInMeters.TERM);
  }
}
