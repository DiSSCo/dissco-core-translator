package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TagTest {

  private final Tag tag = new Tag();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var tagString = "Herbarium | Botany";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:tag", tagString);

    // When
    var result = this.tag.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(tagString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = tag.getTerm();

    // Then
    assertThat(result).isEqualTo(Tag.TERM);
  }

}
