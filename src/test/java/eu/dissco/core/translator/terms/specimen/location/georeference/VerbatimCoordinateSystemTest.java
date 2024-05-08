package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VerbatimCoordinateSystemTest {

  private static final String VERBATIM_COORDINATE_SYSTEM = "degrees decimal minutes";

  private final VerbatimCoordinateSystem verbatimCoordinateSystem = new VerbatimCoordinateSystem();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verbatimCoordinateSystem", VERBATIM_COORDINATE_SYSTEM);

    // When
    var result = verbatimCoordinateSystem.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM_COORDINATE_SYSTEM);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesGrid/gridCellSystem",
        VERBATIM_COORDINATE_SYSTEM);

    // When
    var result = verbatimCoordinateSystem.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM_COORDINATE_SYSTEM);
  }

  @Test
  void testGetTerm() {
    // When
    var result = verbatimCoordinateSystem.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimCoordinateSystem.TERM);
  }
}
