package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChronometricAgeUncertaintyInYearsTest {

  private static final String UNCERTAINTY_IN_YEARS = "1500";
  private final ChronometricAgeUncertaintyInYears chronometricAgeUncertaintyInYears = new ChronometricAgeUncertaintyInYears();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:chronometricAgeUncertaintyInYears", UNCERTAINTY_IN_YEARS);

    // When
    var result = chronometricAgeUncertaintyInYears.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(UNCERTAINTY_IN_YEARS);
  }

  @Test
  void testGetTerm() {
    // When
    var result = chronometricAgeUncertaintyInYears.getTerm();

    // Then
    assertThat(result).isEqualTo(ChronometricAgeUncertaintyInYears.TERM);
  }
}
