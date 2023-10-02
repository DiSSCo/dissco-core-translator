package eu.dissco.core.translator.terms.specimen.occurence;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BehaviorTest {

  private static final String BEHAVIOR_STRING = "foraging";

  private final Behavior behavior = new Behavior();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:behavior", BEHAVIOR_STRING);

    // When
    var result = behavior.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(BEHAVIOR_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = behavior.getTerm();

    // Then
    assertThat(result).isEqualTo(Behavior.TERM);
  }

}
