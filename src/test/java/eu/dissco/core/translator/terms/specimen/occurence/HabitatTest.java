package eu.dissco.core.translator.terms.specimen.occurence;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HabitatTest {

  private static final String HABITAT_STRING = "Forest";

  private final Habitat habitat = new Habitat();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:habitat", HABITAT_STRING);

    // When
    var result = habitat.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(HABITAT_STRING);
  }
  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/biotope/text/value", HABITAT_STRING);

    // When
    var result = habitat.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(HABITAT_STRING);
  }
  @Test
  void testGetTerm() {
    // When
    var result = habitat.getTerm();

    // Then
    assertThat(result).isEqualTo(Habitat.TERM);
  }

}
