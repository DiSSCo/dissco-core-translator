package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChronometricAgeReferencesTest {

  private static final String REFERENCE = "https://doi.org/10.1007/s10814-019-09140-x";
  private final ChronometricAgeReferences chronometricAgeReferences = new ChronometricAgeReferences();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:chronometricAgeReferences", REFERENCE);

    // When
    var result = chronometricAgeReferences.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(REFERENCE);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("analysisReferences/uri", REFERENCE);

    // When
    var result = chronometricAgeReferences.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(REFERENCE);
  }

  @Test
  void testGetTerm() {
    // When
    var result = chronometricAgeReferences.getTerm();

    // Then
    assertThat(result).isEqualTo(ChronometricAgeReferences.TERM);
  }
}
