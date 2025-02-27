package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IsKnownToContainMediaTest {

  private static final String MEDIA_URL = "https://archimg.mnhn.lu/Collections/Collections/ZS536.JPG";

  private final IsKnownToContainMedia isKnownToContainMedia = new IsKnownToContainMedia();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:associatedMedia", MEDIA_URL);

    // When
    var result = isKnownToContainMedia.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("true");
  }

  @Test
  void testRetrieveFromDWCAFalse() {
    // Given
    var unit = MAPPER.createObjectNode();

    // When
    var result = isKnownToContainMedia.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("false");
  }

  @ParameterizedTest
  @ValueSource(strings = {"gbif:Multimedia", "http://rs.tdwg.org/ac/terms/Multimedia"})
  void testRetrieveFromDWCAExtension(String extensionName) {
    // Given
    var unit = MAPPER.createObjectNode();
    var array = MAPPER.createArrayNode();
    var image = MAPPER.createObjectNode();
    array.add(image);
    var extensions = MAPPER.createObjectNode();
    extensions.set(extensionName, array);
    unit.set("extensions", extensions);

    // When
    var result = isKnownToContainMedia.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("true");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:multiMediaObjects/multiMediaObject/0/fileURI", MEDIA_URL);

    // When
    var result = isKnownToContainMedia.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("true");
  }

  @Test
  void testRetrieveFromABCDNoMedia() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("", MEDIA_URL);

    // When
    var result = isKnownToContainMedia.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("false");
  }

  @Test
  void testGetTerm() {
    // When
    var result = isKnownToContainMedia.getTerm();

    // Then
    assertThat(result).isEqualTo(IsKnownToContainMedia.TERM);
  }

}
