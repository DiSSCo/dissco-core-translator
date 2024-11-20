package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChronometricAgeDeterminedByTest {

  private static final String AGE_DETERMINED_BY_STRING = "Tom Dijkema";
  private final ChronometricAgeDeterminedBy chronometricAgeDeterminedBy = new ChronometricAgeDeterminedBy();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:chronometricAgeDeterminedBy", AGE_DETERMINED_BY_STRING);

    // When
    var result = chronometricAgeDeterminedBy.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(AGE_DETERMINED_BY_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = chronometricAgeDeterminedBy.getTerm();

    // Then
    assertThat(result).isEqualTo(ChronometricAgeDeterminedBy.TERM);
  }
}
