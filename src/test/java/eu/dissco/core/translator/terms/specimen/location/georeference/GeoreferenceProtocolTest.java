package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoreferenceProtocolTest {

  private static final String GEOREFERENCE_PROTOCOL = "Georeferencing Quick Reference Guide (Zermoglio et al. 2020, https://doi.org/10.35035/e09p-h128)";

  private final GeoreferenceProtocol georeferenceProtocol = new GeoreferenceProtocol();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:georeferenceProtocol", GEOREFERENCE_PROTOCOL);

    // When
    var result = georeferenceProtocol.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(GEOREFERENCE_PROTOCOL);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinateMethod",
        GEOREFERENCE_PROTOCOL);

    // When
    var result = georeferenceProtocol.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(GEOREFERENCE_PROTOCOL);
  }

  @Test
  void testGetTerm() {
    // When
    var result = georeferenceProtocol.getTerm();

    // Then
    assertThat(result).isEqualTo(GeoreferenceProtocol.TERM);
  }

}
