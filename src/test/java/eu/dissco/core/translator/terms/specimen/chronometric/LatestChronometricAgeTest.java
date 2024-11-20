package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LatestChronometricAgeTest {

  private static final String LATEST_AGE = "100";
  private final LatestChronometricAge latestChronometricAge = new LatestChronometricAge();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:latestChronometricAge", LATEST_AGE);

    // When
    var result = latestChronometricAge.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(LATEST_AGE);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("latestDate", LATEST_AGE);

    // When
    var result = latestChronometricAge.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(LATEST_AGE);
  }

  @Test
  void testGetTerm() {
    // When
    var result = latestChronometricAge.getTerm();

    // Then
    assertThat(result).isEqualTo(LatestChronometricAge.TERM);
  }
}
