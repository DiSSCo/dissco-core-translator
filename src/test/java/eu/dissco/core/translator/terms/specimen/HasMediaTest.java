package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HasMediaTest {

  private static final String MEDIA_URL = "https://archimg.mnhn.lu/Collections/Collections/ZS536.JPG";

  private final HasMedia hasMedia = new HasMedia();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("dwc:associatedMedia", MEDIA_URL);

    // When
    var result = hasMedia.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("true");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:multiMediaObjects/multiMediaObject/0/fileURI", MEDIA_URL);

    // When
    var result = hasMedia.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("true");
  }

  @Test
  void testRetrieveFromABCDNoMedia() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("", MEDIA_URL);

    // When
    var result = hasMedia.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("false");
  }

  @Test
  void testGetTerm() {
    // When
    var result = hasMedia.getTerm();

    // Then
    assertThat(result).isEqualTo(HasMedia.TERM);
  }

}
