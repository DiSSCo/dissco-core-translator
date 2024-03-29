package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PreparationsTest {

  private final Preparations preparations = new Preparations();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var objectTypeString = "alcohol jar";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:preparations", objectTypeString);

    // When
    var result = preparations.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(objectTypeString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var objectTypeString = "alcohol jar";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:kindOfUnit/0/value", objectTypeString);

    // When
    var result = preparations.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(objectTypeString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = preparations.getTerm();

    // Then
    assertThat(result).isEqualTo(Preparations.TERM);
  }

}
