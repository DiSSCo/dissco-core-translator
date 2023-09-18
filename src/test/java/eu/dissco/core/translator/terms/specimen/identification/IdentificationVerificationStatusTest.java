package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
