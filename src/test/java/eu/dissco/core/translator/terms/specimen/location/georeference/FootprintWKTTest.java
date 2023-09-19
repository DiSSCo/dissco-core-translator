package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FootprintWKTTest {

  private static final String FOOTPRINT_WKT_STRING = "POLYGON ((10 20, 11 20, 11 21, 10 21, 10 20))";

  private final FootprintWkt footprintWkt = new FootprintWkt();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:footprintWkt", FOOTPRINT_WKT_STRING);

    // When
    var result = footprintWkt.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(FOOTPRINT_WKT_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = footprintWkt.getTerm();

    // Then
    assertThat(result).isEqualTo(FootprintWkt.TERM);
  }

}
