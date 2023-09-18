package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdentificationIdTest {

  private final IdentificationId identificationId = new IdentificationId();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var identificationIdString = "NBC-31249";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:identificationID", identificationIdString);

    // When
    var result = identificationId.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(identificationIdString);
  }
  @Test
  void testGetTerm() {
    // When
    var result = identificationId.getTerm();

    // Then
    assertThat(result).isEqualTo(IdentificationId.TERM);
  }

}
