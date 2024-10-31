package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FrameRateTest {

  private final FrameRate frameRate = new FrameRate();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var availableString = "60";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:frameRate", availableString);

    // When
    var result = this.frameRate.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(availableString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = frameRate.getTerm();

    // Then
    assertThat(result).isEqualTo(FrameRate.TERM);
  }

}
