package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoReferenceSourcesTest {

  private static final String GEOREFERENCE_SOURCES = "https://www.geonames.org/";

  private final GeoreferenceSources georeferenceSources = new GeoreferenceSources();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:georeferenceSources", GEOREFERENCE_SOURCES);

    // When
    var result = georeferenceSources.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(GEOREFERENCE_SOURCES);
  }

  @Test
  void testGetTerm() {
    // When
    var result = georeferenceSources.getTerm();

    // Then
    assertThat(result).isEqualTo(GeoreferenceSources.TERM);
  }

}
