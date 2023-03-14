package eu.dissco.core.translator.terms.media;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccessUriTest {

  private final AccessUri accessUri = new AccessUri();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var accessUriString = "https://accessuri.eu/image_1";
    var unit = new ObjectMapper().createObjectNode();
    unit.put("ac:accessURI", accessUriString);

    // When
    var result = accessUri.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(accessUriString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var accessUriString = "https://accessuri.eu/image_1";
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:fileURI", accessUriString);

    // When
    var result = accessUri.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(accessUriString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = accessUri.getTerm();

    // Then
    assertThat(result).isEqualTo(AccessUri.TERM);
  }

}
