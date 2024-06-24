package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReproductiveConditionTest {

  private static final String REPRODUCTIVE_CONDITION_STRING = "in bloom";

  private final ReproductiveCondition reproductiveCondition = new ReproductiveCondition();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:reproductiveCondition", REPRODUCTIVE_CONDITION_STRING);

    // When
    var result = reproductiveCondition.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(REPRODUCTIVE_CONDITION_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = reproductiveCondition.getTerm();

    // Then
    assertThat(result).isEqualTo(ReproductiveCondition.TERM);
  }

}
