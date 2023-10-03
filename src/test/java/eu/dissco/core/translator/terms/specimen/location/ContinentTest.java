package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContinentTest {

  private static final String CONTINENT_STRING = "Europe";

  private final Continent continent = new Continent();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:continent", CONTINENT_STRING);

    // When
    var result = continent.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(CONTINENT_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaClass/value", "Continent");
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaName/value", CONTINENT_STRING);

    // When
    var result = continent.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(CONTINENT_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = continent.getTerm();

    // Then
    assertThat(result).isEqualTo(Continent.TERM);
  }

}
