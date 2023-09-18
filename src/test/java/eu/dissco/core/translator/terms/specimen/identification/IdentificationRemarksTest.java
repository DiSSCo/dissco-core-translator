package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdentificationRemarksTest {

  private final IdentificationRemarks identificationRemarks = new IdentificationRemarks();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var identificationRemarkString = "A remark about the identification";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:identificationRemarks", identificationRemarkString);

    // When
    var result = identificationRemarks.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(identificationRemarkString);
  }
  @Test
  void testRetrieveFromABCD() {
    // Given
    var identificationRemarkString = "A remark about the identification";
    var unit = MAPPER.createObjectNode();
    unit.put("notes/value", identificationRemarkString);

    // When
    var result = identificationRemarks.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(identificationRemarkString);
  }
  @Test
  void testGetTerm() {
    // When
    var result = identificationRemarks.getTerm();

    // Then
    assertThat(result).isEqualTo(IdentificationRemarks.TERM);
  }

}
