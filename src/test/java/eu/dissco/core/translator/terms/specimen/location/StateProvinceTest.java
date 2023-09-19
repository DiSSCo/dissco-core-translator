package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.kafka.common.protocol.types.Field.Str;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StateProvinceTest {
private static final String STATE_PROVINCE_STRING = "Brittany";
  private final StateProvince stateProvince = new StateProvince();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:stateProvince", STATE_PROVINCE_STRING);

    // When
    var result = stateProvince.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(STATE_PROVINCE_STRING);
  }
  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaClass/value", "Province");
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaName/value", STATE_PROVINCE_STRING);

    // When
    var result = stateProvince.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(STATE_PROVINCE_STRING);
  }
  @Test
  void testGetTerm() {
    // When
    var result = stateProvince.getTerm();

    // Then
    assertThat(result).isEqualTo(StateProvince.TERM);
  }
}
