package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FootprintSrsTest {

  private static final String FOOTPRINT_SRS_STRING = "epsg:4326";

  private final FootprintSrs footprintSrs = new FootprintSrs();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:footprintSrs", FOOTPRINT_SRS_STRING);

    // When
    var result = footprintSrs.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(FOOTPRINT_SRS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = footprintSrs.getTerm();

    // Then
    assertThat(result).isEqualTo(FootprintSrs.TERM);
  }

}
