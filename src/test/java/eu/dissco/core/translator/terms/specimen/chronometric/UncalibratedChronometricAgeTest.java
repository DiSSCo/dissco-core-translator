package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UncalibratedChronometricAgeTest {

  private static final String UNCALIBRATED = "1510 +/- 25 14C yr BP";
  private final UncalibratedChronometricAge uncalibratedChronometricAge = new UncalibratedChronometricAge();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:uncalibratedChronometricAge", UNCALIBRATED);

    // When
    var result = uncalibratedChronometricAge.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(UNCALIBRATED);
  }

  @Test
  void testGetTerm() {
    // When
    var result = uncalibratedChronometricAge.getTerm();

    // Then
    assertThat(result).isEqualTo(UncalibratedChronometricAge.TERM);
  }
}
