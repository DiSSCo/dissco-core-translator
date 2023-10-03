package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IslandGroupTest {

  private static final String ISLAND_GROUP_STRING = "Harrison Islands";
  private final IslandGroup islandGroup = new IslandGroup();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:islandGroup", ISLAND_GROUP_STRING);

    // When
    var result = islandGroup.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(ISLAND_GROUP_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaClass/value", "Island group");
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaName/value", ISLAND_GROUP_STRING);

    // When
    var result = islandGroup.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(ISLAND_GROUP_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = islandGroup.getTerm();

    // Then
    assertThat(result).isEqualTo(IslandGroup.TERM);
  }
}
