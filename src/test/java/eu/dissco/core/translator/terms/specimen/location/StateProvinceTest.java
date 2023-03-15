package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StateProvinceTest {

  private final StateProvince stateProvince = new StateProvince();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var stateProvinceString = "Brittany";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:stateProvince", stateProvinceString);

    // When
    var result = stateProvince.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(stateProvinceString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = stateProvince.getTerm();

    // Then
    assertThat(result).isEqualTo(StateProvince.TERM);
  }
}
