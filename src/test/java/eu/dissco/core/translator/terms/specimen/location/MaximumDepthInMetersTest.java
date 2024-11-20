package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaximumDepthInMetersTest {

  private static final String MAXIMUM_DEPTH_IN_METERS_STRING = "-350";
  private final MaximumDepthInMeters maximumDepthInMeters = new MaximumDepthInMeters();

  @ParameterizedTest
  @ValueSource(strings = {"-350m.", "-350meter", "-350 m", "-350 MTR"})
  void testRetrieveFromDWCA(String input) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:maximumDepthInMeters", input);

    // When
    var result = maximumDepthInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MAXIMUM_DEPTH_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromDWCANull() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("dwc:maximumDepthInMeters");

    // When
    var result = maximumDepthInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isNull();
  }

  @ParameterizedTest
  @ValueSource(strings = {"-350", "-350.0", "-350.00", "-350.000"})
  void testRetrieveFromABCD(String input) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/depth/measurementOrFactAtomised/upperValue/value",
        input);

    // When
    var result = maximumDepthInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MAXIMUM_DEPTH_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromABCDNull() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("abcd:gathering/depth/measurementOrFactAtomised/upperValue/value");

    // When
    var result = maximumDepthInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = maximumDepthInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(MaximumDepthInMeters.TERM);
  }
}
