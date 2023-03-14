package eu.dissco.core.translator.terms.specimen.location;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    var unit = new ObjectMapper().createObjectNode();
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
