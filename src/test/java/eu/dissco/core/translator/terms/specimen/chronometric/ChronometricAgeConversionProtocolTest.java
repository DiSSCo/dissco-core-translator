package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChronometricAgeConversionProtocolTest {

  private static final String AGE_CONVERSION_PROTOCOL_STRING = "INTCAL13";
  private final ChronometricAgeConversionProtocol chronometricAgeConversionProtocol = new ChronometricAgeConversionProtocol();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:chronometricAgeConversionProtocol", AGE_CONVERSION_PROTOCOL_STRING);

    // When
    var result = chronometricAgeConversionProtocol.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(AGE_CONVERSION_PROTOCOL_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = chronometricAgeConversionProtocol.getTerm();

    // Then
    assertThat(result).isEqualTo(ChronometricAgeConversionProtocol.TERM);
  }
}
