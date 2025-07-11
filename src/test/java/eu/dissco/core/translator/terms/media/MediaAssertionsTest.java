package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MediaAssertionsTest {

  private final MediaAssertions assertions = new MediaAssertions();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();

    // When
    var result = assertions.gatherAssertions(unit, true);

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:fileSize", "38");
    unit.put("abcd:imageSize/width", "500");
    unit.put("abcd:imageSize/height", 1500);

    // When
    var result = assertions.gatherAssertions(unit, false);

    // Then
    assertThat(result).hasSize(3);
  }
}
