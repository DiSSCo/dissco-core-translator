package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FormatTest {

  private final Format format = new Format();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var formatString = "StillImage";
    var unit = MAPPER.createObjectNode();
    unit.putNull("dcterms:format");
    unit.put("dc:format", formatString);

    // When
    var result = format.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(formatString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var formatString = "SpecimenName";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:format", formatString);

    // When
    var result = format.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(formatString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = format.getTerm();

    // Then
    assertThat(result).isEqualTo(Format.TERM);
  }

}
