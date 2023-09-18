package eu.dissco.core.translator.terms.specimen.citation;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TypeTest {

  private final Type type = new Type();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var typeString = "Publication";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:type", typeString);

    // When
    var result = type.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(typeString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = type.getTerm();

    // Then
    assertThat(result).isEqualTo(Type.TERM);
  }

}
