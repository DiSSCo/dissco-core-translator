package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccessURITest {

  private final AccessURI accessUri = new AccessURI();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var accessUriString = "https://accessuri.eu/image_1";
    var unit = MAPPER.createObjectNode();
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
    var unit = MAPPER.createObjectNode();
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
    assertThat(result).isEqualTo(AccessURI.TERM);
  }

}
