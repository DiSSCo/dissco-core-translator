package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WaterBodyTest {

  private static final String WATER_BODY_STRING = "The Aegean Sea";

  private final WaterBody waterBody = new WaterBody();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:waterBody", WATER_BODY_STRING);

    // When
    var result = waterBody.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(WATER_BODY_STRING);
  }
  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaClass/value", "Water body");
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaName/value", WATER_BODY_STRING);

    // When
    var result = waterBody.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(WATER_BODY_STRING);
  }
  @Test
  void testGetTerm() {
    // When
    var result = waterBody.getTerm();

    // Then
    assertThat(result).isEqualTo(WaterBody.TERM);
  }

}
