package eu.dissco.core.translator.terms.specimen.location;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocalityTest {

  private static final String LOCALITY_STRING =
      "Schnegaer Mühlenbach, Dümme-System, between Wöhningen and Brückau, Dümme-Elbe System, "
          + "Niedersachsen/Lower Saxony";

  private final Locality locality = new Locality();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("dwc:locality", LOCALITY_STRING);

    // When
    var result = locality.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(LOCALITY_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:gathering/localityText/value", LOCALITY_STRING);

    // When
    var result = locality.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(LOCALITY_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = locality.getTerm();

    // Then
    assertThat(result).isEqualTo(Locality.TERM);
  }
}
