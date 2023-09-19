package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointRadiusSpatialFitTest {

  private static final String POINT_RADIUS_SPATIAL_FIT_STRING = "1.5708";

  private final PointRadiusSpatialFit pointRadiusSpatialFit = new PointRadiusSpatialFit();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:pointRadiusSpatialFit", POINT_RADIUS_SPATIAL_FIT_STRING);

    // When
    var result = pointRadiusSpatialFit.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(POINT_RADIUS_SPATIAL_FIT_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = pointRadiusSpatialFit.getTerm();

    // Then
    assertThat(result).isEqualTo(PointRadiusSpatialFit.TERM);
  }

}
