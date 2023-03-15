package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MunicipalityTest {

  private final Municipality municipality = new Municipality();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var municipalityString = "Gouda";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:municipality", municipalityString);

    // When
    var result = municipality.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(municipalityString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = municipality.getTerm();

    // Then
    assertThat(result).isEqualTo(Municipality.TERM);
  }

}
