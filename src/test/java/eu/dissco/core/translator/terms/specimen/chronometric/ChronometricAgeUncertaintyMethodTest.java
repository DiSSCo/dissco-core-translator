package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChronometricAgeUncertaintyMethodTest {

  private static final String UNCERTAINTY_METHOD = "Half of 95% confidence interval";
  private final ChronometricAgeUncertaintyMethod chronometricAgeUncertaintyMethod = new ChronometricAgeUncertaintyMethod();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:chronometricAgeUncertaintyMethod", UNCERTAINTY_METHOD);

    // When
    var result = chronometricAgeUncertaintyMethod.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(UNCERTAINTY_METHOD);
  }

  @Test
  void testGetTerm() {
    // When
    var result = chronometricAgeUncertaintyMethod.getTerm();

    // Then
    assertThat(result).isEqualTo(ChronometricAgeUncertaintyMethod.TERM);
  }
}
