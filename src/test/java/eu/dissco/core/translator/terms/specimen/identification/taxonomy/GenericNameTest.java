package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GenericNameTest {

  private final GenericName genericName = new GenericName();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var genericNameString = "Spiranthes";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:genericName", genericNameString);

    // When
    var result = genericName.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(genericNameString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = genericName.getTerm();

    // Then
    assertThat(result).isEqualTo(GenericName.TERM);
  }
}
