package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MetadataLanguageLiteralTest {

  private final MetadataLanguageLiteral metadataLanguageLiteral = new MetadataLanguageLiteral();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var metadataLanguageLiteralString = "eng";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:metadataLanguageLiteral", metadataLanguageLiteralString);

    // When
    var result = this.metadataLanguageLiteral.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(metadataLanguageLiteralString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = metadataLanguageLiteral.getTerm();

    // Then
    assertThat(result).isEqualTo(MetadataLanguageLiteral.TERM);
  }

}
