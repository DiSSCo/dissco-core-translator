package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VariantTest {

  private final Variant variant = new Variant();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var variantString = "http://rs.tdwg.org/acvariant/values/v005";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:variant", variantString);

    // When
    var result = this.variant.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(variantString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = variant.getTerm();

    // Then
    assertThat(result).isEqualTo(Variant.TERM);
  }

}
