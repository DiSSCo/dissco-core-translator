package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VariantDescriptionTest {

  private final VariantDescription variantDescription = new VariantDescription();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var variantDescriptionString = "Video shortened instead of simply quality reduced";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:variantDescription", variantDescriptionString);

    // When
    var result = this.variantDescription.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(variantDescriptionString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = variantDescription.getTerm();

    // Then
    assertThat(result).isEqualTo(VariantDescription.TERM);
  }

}
