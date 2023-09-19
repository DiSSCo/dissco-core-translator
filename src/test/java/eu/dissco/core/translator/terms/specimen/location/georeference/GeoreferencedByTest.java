package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoreferencedByTest {

  private static final String GEOREFERENCED_BY = "S. Leeflang";

  private final GeoreferencedBy georeferencedBy = new GeoreferencedBy();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:georeferencedBy", GEOREFERENCED_BY);

    // When
    var result = georeferencedBy.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(GEOREFERENCED_BY);
  }

  @Test
  void testGetTerm() {
    // When
    var result = georeferencedBy.getTerm();

    // Then
    assertThat(result).isEqualTo(GeoreferencedBy.TERM);
  }

}
