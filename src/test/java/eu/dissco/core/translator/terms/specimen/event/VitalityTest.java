package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VitalityTest {

  private static final String VITALITY_STRING = "alive";

  private final Vitality vitality = new Vitality();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:vitality", VITALITY_STRING);

    // When
    var result = vitality.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(VITALITY_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = vitality.getTerm();

    // Then
    assertThat(result).isEqualTo(Vitality.TERM);
  }

}
