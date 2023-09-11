package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.terms.specimen.occurence.FieldNumber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CollectingNumberTest {

  private static final String NUMBER = "245";

  private final FieldNumber collectingNumber = new FieldNumber();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:recordNumber", NUMBER);

    // When
    var result = collectingNumber.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(NUMBER);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:collectorsFieldNumber", NUMBER);

    // When
    var result = collectingNumber.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(NUMBER);
  }

  @Test
  void testGetTerm() {
    // When
    var result = collectingNumber.getTerm();

    // Then
    assertThat(result).isEqualTo(FieldNumber.TERM);
  }

}
