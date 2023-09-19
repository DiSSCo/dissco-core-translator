package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IslandTest {

  private static final String ISLAND_STRING = "Texel";
  private final Island island = new Island();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:island", ISLAND_STRING);

    // When
    var result = island.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(ISLAND_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaClass/value", "Island");
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaName/value", ISLAND_STRING);

    // When
    var result = island.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(ISLAND_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = island.getTerm();

    // Then
    assertThat(result).isEqualTo(Island.TERM);
  }
}
