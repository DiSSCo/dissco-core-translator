package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.terms.specimen.location.georeference.GeodeticDatum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeodeticDatumTest {

  private static final String GEODETIC_DATUM_STRING = "WGS84";

  private final GeodeticDatum geodeticDatum = new GeodeticDatum();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:geodeticDatum", GEODETIC_DATUM_STRING);

    // When
    var result = geodeticDatum.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(GEODETIC_DATUM_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/spatialDatum",
        GEODETIC_DATUM_STRING);

    // When
    var result = geodeticDatum.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(GEODETIC_DATUM_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = geodeticDatum.getTerm();

    // Then
    assertThat(result).isEqualTo(GeodeticDatum.TERM);
  }

}
