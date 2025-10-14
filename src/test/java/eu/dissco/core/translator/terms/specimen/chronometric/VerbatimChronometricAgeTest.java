package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerbatimChronometricAgeTest {

  private static final String VERBATIM = "27 BC to 14 AD";
  private final VerbatimChronometricAge uncalibratedChronometricAge = new VerbatimChronometricAge();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:verbatimChronometricAge", VERBATIM);

    // When
    var result = uncalibratedChronometricAge.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("datingAccuracy", VERBATIM);

    // When
    var result = uncalibratedChronometricAge.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM);
  }

  @Test
  void testGetTerm() {
    // When
    var result = uncalibratedChronometricAge.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimChronometricAge.TERM);
  }
}
