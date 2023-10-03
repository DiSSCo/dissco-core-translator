package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreatorTest {

  private final Creator creator = new Creator();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var creatorString = "Sam Leeflang";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:creator", creatorString);

    // When
    var result = creator.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(creatorString);
  }

  @Test
  void testRetrieveFromAbcd() {
    // Given
    var createdString = "Sam Leeflang";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:creator", createdString);

    // When
    var result = creator.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(createdString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = creator.getTerm();

    // Then
    assertThat(result).isEqualTo(Creator.TERM);
  }

}
