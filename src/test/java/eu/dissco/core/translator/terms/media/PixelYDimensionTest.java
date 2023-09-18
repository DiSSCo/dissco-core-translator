package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PixelYDimensionTest {

  private final PixelYDimension pixelYDimension = new PixelYDimension();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var pixelyDimensionString = "1500";
    var unit = MAPPER.createObjectNode();
    unit.put("exif:PixelYDimension", pixelyDimensionString);

    // When
    var result = pixelYDimension.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(pixelyDimensionString);
  }

  @Test
  void testRetrieveFromAbcd() {
    // Given
    var pixelyDimensionString = "1500";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:imageSize/height", pixelyDimensionString);

    // When
    var result = pixelYDimension.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(pixelyDimensionString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = pixelYDimension.getTerm();

    // Then
    assertThat(result).isEqualTo(PixelYDimension.TERM);
  }

}
