package eu.dissco.core.translator.terms.specimen.location;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IslandGroupTest {

  private final IslandGroup islandGroup = new IslandGroup();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var islandGroupString = "Harrison Islands";
    var unit = new ObjectMapper().createObjectNode();
    unit.put("dwc:islandGroup", islandGroupString);

    // When
    var result = islandGroup.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(islandGroupString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = islandGroup.getTerm();

    // Then
    assertThat(result).isEqualTo(IslandGroup.TERM);
  }
}
