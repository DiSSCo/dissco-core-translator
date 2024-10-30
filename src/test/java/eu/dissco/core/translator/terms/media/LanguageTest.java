package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LanguageTest {

  private final Language language = new Language();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var languageString = "nld";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:language", languageString);

    // When
    var result = this.language.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(languageString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = language.getTerm();

    // Then
    assertThat(result).isEqualTo(Language.TERM);
  }

}
