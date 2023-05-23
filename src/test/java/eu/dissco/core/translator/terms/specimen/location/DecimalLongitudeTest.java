package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DecimalLongitudeTest {

  private static final String LONGITUDE_STRING = "10.48778";

  private final DecimalLongitude decimalLongitude = new DecimalLongitude();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:decimalLongitude", LONGITUDE_STRING);

    // When
    var result = decimalLongitude.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(LONGITUDE_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/longitudeDecimal",
        10.48778);

    // When
    var result = decimalLongitude.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(LONGITUDE_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = decimalLongitude.getTerm();

    // Then
    assertThat(result).isEqualTo(DecimalLongitude.TERM);
  }

}
