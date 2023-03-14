package eu.dissco.core.translator.terms.media;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MediaTypeTest {

  private final MediaType mediaType = new MediaType();

  @ParameterizedTest
  @MethodSource("provideACTypes")
  void testRetrieveFromDWCA(String acType, String expected) {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("dcterms:type", acType);

    // When
    var result = mediaType.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  private static Stream<Arguments> provideACTypes(){
    return Stream.of(
        Arguments.of("StillImage", "2DImageObject"),
        Arguments.of("Image", "2DImageObject"),
        Arguments.of("Sound", "AudioObject"),
        Arguments.of("MovingImage", "VideoObject"),
        Arguments.of("RandomString", null)
    );
  }

  @ParameterizedTest
  @MethodSource("provideFormatTypes")
  void testRetrieveFromDWCANoType(String format, String expected) {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.set("dcterms:type", null);
    unit.set("dc:type", null);
    unit.put("dcterms:format", format);

    // When
    var result = mediaType.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  private static Stream<Arguments> provideFormatTypes(){
    return Stream.of(
        Arguments.of("image/jpeg", "2DImageObject"),
        Arguments.of("audio/example", "AudioObject"),
        Arguments.of("video/example", "VideoObject"),
        Arguments.of("RandomString", null)
    );
  }

  @ParameterizedTest
  @MethodSource("provideABCDFormats")
  void testRetrieveFromABCD(String format, String expected) {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    if (format != null) {
      unit.put("abcd:format", format);
    }

    // When
    var result = mediaType.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  private static Stream<Arguments> provideABCDFormats(){
    return Stream.of(
        Arguments.of("image/jpeg", "2DImageObject"),
        Arguments.of("unknown/format", null),
        Arguments.of(null, null)
    );
  }

  @Test
  void testGetTerm() {
    // When
    var result = mediaType.getTerm();

    // Then
    assertThat(result).isEqualTo(MediaType.TERM);
  }

}
