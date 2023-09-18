package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SourceTest {

  private final Source source = new Source();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var sourceString = "https://geocollections.info/file/168145";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:source", sourceString);

    // When
    var result = source.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(sourceString);
  }

  @Test
  void testRetrieveFromAbcd() {
    // Given
    var sourceString = "https://geocollections.info/file/168145";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:productURI", sourceString);

    // When
    var result = source.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(sourceString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = source.getTerm();

    // Then
    assertThat(result).isEqualTo(Source.TERM);
  }

}
