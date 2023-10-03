package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoordinateUncertaintyInMetersTest {

  private static final String COORDINATE_UNCERTAINTY_IN_METERS = "4.6050";

  private final CoordinateUncertaintyInMeters coordinateUncertaintyInMeters = new CoordinateUncertaintyInMeters();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:coordinateUncertaintyInMeters", COORDINATE_UNCERTAINTY_IN_METERS);

    // When
    var result = coordinateUncertaintyInMeters.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(COORDINATE_UNCERTAINTY_IN_METERS);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/coordinateErrorDistanceInMeters",
        COORDINATE_UNCERTAINTY_IN_METERS);

    // When
    var result = coordinateUncertaintyInMeters.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(COORDINATE_UNCERTAINTY_IN_METERS);
  }

  @Test
  void testGetTerm() {
    // When
    var result = coordinateUncertaintyInMeters.getTerm();

    // Then
    assertThat(result).isEqualTo(CoordinateUncertaintyInMeters.TERM);
  }
}
