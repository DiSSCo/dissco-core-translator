package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IslandTest {

  private final Island island = new Island();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var islandString = "Texel";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:island", islandString);

    // When
    var result = island.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(islandString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = island.getTerm();

    // Then
    assertThat(result).isEqualTo(Island.TERM);
  }
}
