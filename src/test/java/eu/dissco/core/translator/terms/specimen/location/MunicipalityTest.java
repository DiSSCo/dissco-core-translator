package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MunicipalityTest {

  private static final String MUNICIPALITY_STRING = "Gouda";
  private final Municipality municipality = new Municipality();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:municipality", MUNICIPALITY_STRING);

    // When
    var result = municipality.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MUNICIPALITY_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaClass/value", "Municipality");
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaName/value", MUNICIPALITY_STRING);

    // When
    var result = municipality.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MUNICIPALITY_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = municipality.getTerm();

    // Then
    assertThat(result).isEqualTo(Municipality.TERM);
  }

}
