package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PixelXDimensionTest {

  private final FileSize fileSize = new FileSize();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var fileSizeString = "38";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:extent", fileSizeString);

    // When
    var result = fileSize.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(fileSizeString);
  }

  @Test
  void testRetrieveFromAbcd() {
    // Given
    var fileSizeString = "38";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:fileSize", fileSizeString);

    // When
    var result = fileSize.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(fileSizeString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = fileSize.getTerm();

    // Then
    assertThat(result).isEqualTo(FileSize.TERM);
  }

}
