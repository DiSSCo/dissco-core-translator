package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EarliestChronometricAgeReferenceSystemTest {

  private static final String EARLIEST_AGE_REFERENCE_SYSTEM = "BP";
  private final EarliestChronometricAgeReferenceSystem earliestChronometricAgeReferenceSystem = new EarliestChronometricAgeReferenceSystem();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:earliestChronometricAgeReferenceSystem", EARLIEST_AGE_REFERENCE_SYSTEM);

    // When
    var result = earliestChronometricAgeReferenceSystem.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(EARLIEST_AGE_REFERENCE_SYSTEM);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("timeUnit", EARLIEST_AGE_REFERENCE_SYSTEM);

    // When
    var result = earliestChronometricAgeReferenceSystem.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(EARLIEST_AGE_REFERENCE_SYSTEM);
  }

  @Test
  void testGetTerm() {
    // When
    var result = earliestChronometricAgeReferenceSystem.getTerm();

    // Then
    assertThat(result).isEqualTo(EarliestChronometricAgeReferenceSystem.TERM);
  }
}
