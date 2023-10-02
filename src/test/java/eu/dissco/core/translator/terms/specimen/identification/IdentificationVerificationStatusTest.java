package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdentificationVerificationStatusTest {

  private final IdentificationVerificationStatus identificationVerificationStatus = new IdentificationVerificationStatus();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var identificationVerificationStatusString = "1";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:identificationVerificationStatus", identificationVerificationStatusString);

    // When
    var result = identificationVerificationStatus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("true");
  }

  @ParameterizedTest
  @ValueSource(strings = {"0", "some other value"})
  void testRetrieveFromDWCAFalse(String identificationVerificationStatusString) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:identificationVerificationStatus", identificationVerificationStatusString);

    // When
    var result = identificationVerificationStatus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("false");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var identificationVerificationStatusString = "true";
    var unit = MAPPER.createObjectNode();
    unit.put("preferredFlag", identificationVerificationStatusString);

    // When
    var result = identificationVerificationStatus.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(identificationVerificationStatusString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = identificationVerificationStatus.getTerm();

    // Then
    assertThat(result).isEqualTo(IdentificationVerificationStatus.TERM);
  }

}
