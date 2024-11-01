package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TitleTest {

  private final Title title = new Title();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var titleString = "Image of a botanical sheet with label";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:title", titleString);

    // When
    var result = this.title.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(titleString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = title.getTerm();

    // Then
    assertThat(result).isEqualTo(Title.TERM);
  }

}
