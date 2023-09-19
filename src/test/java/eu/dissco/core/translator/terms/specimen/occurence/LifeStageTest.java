package eu.dissco.core.translator.terms.specimen.occurence;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.terms.specimen.location.WaterBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LifeStageTest {

  private static final String LIFE_STAGE_STRING = "Adult";

  private final LifeStage lifeStage = new LifeStage();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:lifeStage", LIFE_STAGE_STRING);

    // When
    var result = lifeStage.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(LIFE_STAGE_STRING);
  }
  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:unit/mycologicalUnit/mycologicalLiveStages/0/mycologicalLiveStage/value", LIFE_STAGE_STRING);

    // When
    var result = lifeStage.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(LIFE_STAGE_STRING);
  }
  @Test
  void testGetTerm() {
    // When
    var result = lifeStage.getTerm();

    // Then
    assertThat(result).isEqualTo(LifeStage.TERM);
  }

}
