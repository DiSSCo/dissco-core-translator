package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileSize {

  private final Description description = new Description();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var descriptionString = "An image of a plant";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:extent", descriptionString);

    // When
    var result = description.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(descriptionString);
  }

  @Test
  void testRetrieveFromAbcd() {
    // Given
    var descriptionString = "An image of a plant";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:context/value", descriptionString);

    // When
    var result = description.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(descriptionString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = description.getTerm();

    // Then
    assertThat(result).isEqualTo(Description.TERM);
  }

}
