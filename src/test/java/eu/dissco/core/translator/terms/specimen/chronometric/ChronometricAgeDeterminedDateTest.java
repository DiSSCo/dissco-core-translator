package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MOCK_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChronometricAgeDeterminedDateTest {

  private final ChronometricAgeDeterminedDate chronometricAgeDeterminedDate = new ChronometricAgeDeterminedDate();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:chronometricAgeDeterminedDate", MOCK_DATE);

    // When
    var result = chronometricAgeDeterminedDate.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MOCK_DATE);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("analysisDateTime/exactDate/dateText", MOCK_DATE);

    // When
    var result = chronometricAgeDeterminedDate.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MOCK_DATE);
  }

  @Test
  void testGetTerm() {
    // When
    var result = chronometricAgeDeterminedDate.getTerm();

    // Then
    assertThat(result).isEqualTo(ChronometricAgeDeterminedDate.TERM);
  }
}
