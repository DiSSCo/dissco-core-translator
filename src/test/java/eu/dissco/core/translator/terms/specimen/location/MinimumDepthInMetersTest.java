package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MinimumDepthInMetersTest {

  private static final String MINIMUM_DEPTH_IN_METERS_STRING = "-50";
  private final MinimumDepthInMeters minimumDepthInMeters = new MinimumDepthInMeters();

  @ParameterizedTest
  @ValueSource(strings = {"-50m.", "-50meter", "-50 m", "-50 MTR"})
  void testRetrieveFromDWCA(String input) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:minimumDepthInMeters", input);

    // When
    var result = minimumDepthInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MINIMUM_DEPTH_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromDWCANull() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("dwc:minimumDepthInMeters");

    // When
    var result = minimumDepthInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isNull();
  }

  @ParameterizedTest
  @ValueSource(strings = {"-50m.", "-50meter", "-50 m", "-50 MTR"})
  void testRetrieveFromABCD(String input) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/depth/measurementOrFactAtomised/lowerValue/value", input);

    // When
    var result = minimumDepthInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MINIMUM_DEPTH_IN_METERS_STRING);
  }

  @Test
  void testRetrieveFromNull() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("abcd:gathering/depth/measurementOrFactAtomised/lowerValue/value");

    // When
    var result = minimumDepthInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = minimumDepthInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(MinimumDepthInMeters.TERM);
  }
}
