package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MetadataLanguageTest {

  private final MetadataLanguage metadataLanguage = new MetadataLanguage();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var metadataLanguageString = "http://id.loc.gov/vocabulary/iso639-2/eng";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:metadataLanguage", metadataLanguageString);

    // When
    var result = this.metadataLanguage.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(metadataLanguageString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = metadataLanguage.getTerm();

    // Then
    assertThat(result).isEqualTo(MetadataLanguage.TERM);
  }

}
