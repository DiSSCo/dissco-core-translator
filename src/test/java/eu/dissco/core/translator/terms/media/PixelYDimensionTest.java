package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PixelYDimensionTest {

  private final PixelXDimension pixelXDimension = new PixelXDimension();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var pixelXDimensionString = "500";
    var unit = MAPPER.createObjectNode();
    unit.put("exif:PixelXDimension", pixelXDimensionString);

    // When
    var result = pixelXDimension.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(pixelXDimensionString);
  }

  @Test
  void testRetrieveFromAbcd() {
    // Given
    var pixelXDimensionString = "500";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:imageSize/width", pixelXDimensionString);

    // When
    var result = pixelXDimension.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(pixelXDimensionString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = pixelXDimension.getTerm();

    // Then
    assertThat(result).isEqualTo(PixelXDimension.TERM);
  }

}
