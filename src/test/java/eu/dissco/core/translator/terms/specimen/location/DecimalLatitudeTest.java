package eu.dissco.core.translator.terms.specimen.location;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DecimalLatitudeTest {

  private static final String LATITUDE_STRING = "53.33167";

  private final DecimalLatitude decimalLatitude = new DecimalLatitude();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("dwc:decimalLatitude", LATITUDE_STRING);

    // When
    var result = decimalLatitude.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(LATITUDE_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put(
        "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/latitudeDecimal",
        LATITUDE_STRING);

    // When
    var result = decimalLatitude.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(LATITUDE_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = decimalLatitude.getTerm();

    // Then
    assertThat(result).isEqualTo(DecimalLatitude.TERM);
  }
}
