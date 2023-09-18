package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdentifiedByTest {

  private final IdentifiedBy identifiedBy = new IdentifiedBy();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var identifiedByString = "Sam Leeflang";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:identifiedBy", identifiedByString);

    // When
    var result = identifiedBy.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(identifiedByString);
  }
  @Test
  void testRetrieveFromABCD() {
    // Given
    var identifiedByString = "Sam Leeflang";
    var unit = MAPPER.createObjectNode();
    unit.put("identifiers/identifier/0/personName/fullName", identifiedByString);

    // When
    var result = identifiedBy.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(identifiedByString);
  }
  @Test
  void testGetTerm() {
    // When
    var result = identifiedBy.getTerm();

    // Then
    assertThat(result).isEqualTo(IdentifiedBy.TERM);
  }

}
