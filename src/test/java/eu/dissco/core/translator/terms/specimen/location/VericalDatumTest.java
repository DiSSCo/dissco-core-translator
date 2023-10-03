package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VericalDatumTest {

  private static final String VERTICAL_DATUM_STRING = "EGM84";

  private final VerticalDatum verticalDatum = new VerticalDatum();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verticalDatum", VERTICAL_DATUM_STRING);

    // When
    var result = verticalDatum.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(VERTICAL_DATUM_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = verticalDatum.getTerm();

    // Then
    assertThat(result).isEqualTo(VerticalDatum.TERM);
  }

}
