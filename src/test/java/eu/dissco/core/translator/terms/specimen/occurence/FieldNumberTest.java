package eu.dissco.core.translator.terms.specimen.occurence;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FieldNumberTest {

  private static final String NUMBER = "245";

  private final FieldNumber fieldNumber = new FieldNumber();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:fieldNumber", NUMBER);

    // When
    var result = fieldNumber.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(NUMBER);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/code", NUMBER);

    // When
    var result = fieldNumber.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(NUMBER);
  }

  @Test
  void testGetTerm() {
    // When
    var result = fieldNumber.getTerm();

    // Then
    assertThat(result).isEqualTo(FieldNumber.TERM);
  }

}
