package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EarliestChronometricAgeTest {

  private static final String EARLIEST_AGE = "100";
  private final EarliestChronometricAge earliestChronometricAge = new EarliestChronometricAge();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:earliestChronometricAge", EARLIEST_AGE);

    // When
    var result = earliestChronometricAge.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(EARLIEST_AGE);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("earliestDate", EARLIEST_AGE);

    // When
    var result = earliestChronometricAge.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(EARLIEST_AGE);
  }

  @Test
  void testGetTerm() {
    // When
    var result = earliestChronometricAge.getTerm();

    // Then
    assertThat(result).isEqualTo(EarliestChronometricAge.TERM);
  }
}
