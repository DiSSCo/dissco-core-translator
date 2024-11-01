package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VariantLiteralTest {

  private final VariantLiteral variantLiteral = new VariantLiteral();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var variantLiteralString = "Thumbnail";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:variantLiteral", variantLiteralString);

    // When
    var result = this.variantLiteral.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(variantLiteralString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = variantLiteral.getTerm();

    // Then
    assertThat(result).isEqualTo(VariantLiteral.TERM);
  }

}
