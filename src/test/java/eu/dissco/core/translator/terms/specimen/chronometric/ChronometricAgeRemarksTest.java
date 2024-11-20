package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChronometricAgeRemarksTest {

  private static final String REMARK = "Beta Analytic number: 323913";
  private final ChronometricAgeRemarks chronometricAgeRemarks = new ChronometricAgeRemarks();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:chronometricAgeRemarks", REMARK);

    // When
    var result = chronometricAgeRemarks.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(REMARK);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("datingComment", REMARK);

    // When
    var result = chronometricAgeRemarks.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(REMARK);
  }

  @Test
  void testGetTerm() {
    // When
    var result = chronometricAgeRemarks.getTerm();

    // Then
    assertThat(result).isEqualTo(ChronometricAgeRemarks.TERM);
  }
}
