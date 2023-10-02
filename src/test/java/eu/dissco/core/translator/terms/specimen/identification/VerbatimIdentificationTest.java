package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerbatimIdentificationTest {

  private final VerbatimIdentification verbatimIdentification = new VerbatimIdentification();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var verbatimIdentificationString = "A verbatim identification";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verbatimIdentification", verbatimIdentificationString);

    // When
    var result = verbatimIdentification.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(verbatimIdentificationString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = verbatimIdentification.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimIdentification.TERM);
  }

}
