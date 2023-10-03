package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FootprintSpatialFitTest {

  private static final String FOOTPRINT_SPATIAL_FIT_STRING = "1";

  private final FootprintSpatialFit footprintSpatialFit = new FootprintSpatialFit();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:footprintSpatialFit", FOOTPRINT_SPATIAL_FIT_STRING);

    // When
    var result = footprintSpatialFit.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(FOOTPRINT_SPATIAL_FIT_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = footprintSpatialFit.getTerm();

    // Then
    assertThat(result).isEqualTo(FootprintSpatialFit.TERM);
  }

}
