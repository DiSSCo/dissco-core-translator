package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoordinatePrecisionTest {

  private static final String COORDINATE_PRECISION_STRING = "0.10";

  private final CoordinatePrecision coordinatePrecision = new CoordinatePrecision();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:coordinatePrecision", COORDINATE_PRECISION_STRING);

    // When
    var result = coordinatePrecision.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(COORDINATE_PRECISION_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/iSOAccuracy",
        COORDINATE_PRECISION_STRING);

    // When
    var result = coordinatePrecision.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(COORDINATE_PRECISION_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = coordinatePrecision.getTerm();

    // Then
    assertThat(result).isEqualTo(CoordinatePrecision.TERM);
  }
}
