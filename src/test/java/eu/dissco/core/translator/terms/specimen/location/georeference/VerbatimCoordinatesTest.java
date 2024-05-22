package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerbatimCoordinatesTest {

  private static final String VERBATIM_COORDINATES = "41 05 54S 121 05 34W";

  private final VerbatimCoordinates verbatimCoordinates = new VerbatimCoordinates();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verbatimCoordinates", VERBATIM_COORDINATES);

    // When
    var result = verbatimCoordinates.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM_COORDINATES);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesUTM/uTMText",
        VERBATIM_COORDINATES);

    // When
    var result = verbatimCoordinates.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM_COORDINATES);
  }

  @Test
  void testGetTerm() {
    // When
    var result = verbatimCoordinates.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimCoordinates.TERM);
  }
}
