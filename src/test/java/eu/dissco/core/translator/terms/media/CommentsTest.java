package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MOCK_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentsTest {

  private final Comments comments = new Comments();
  private final String commentsString = "This object is not yet published";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("ac:comments", commentsString);

    // When
    var result = comments.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(commentsString);
  }

  @Test
  void testRetrieveFromAbcd() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:comment/value", commentsString);

    // When
    var result = comments.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(commentsString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = comments.getTerm();

    // Then
    assertThat(result).isEqualTo(Comments.TERM);
  }

}
