package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChronometricAgeProtocolTest {

  private static final String AGE_PROTOCOL = "radiocarbon";
  private final ChronometricAgeProtocol chronometricAgeProtocol = new ChronometricAgeProtocol();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:chronometricAgeProtocol", AGE_PROTOCOL);

    // When
    var result = chronometricAgeProtocol.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(AGE_PROTOCOL);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("radiometricDatingMethod", AGE_PROTOCOL);

    // When
    var result = chronometricAgeProtocol.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(AGE_PROTOCOL);
  }

  @Test
  void testGetTerm() {
    // When
    var result = chronometricAgeProtocol.getTerm();

    // Then
    assertThat(result).isEqualTo(ChronometricAgeProtocol.TERM);
  }
}
