package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

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

  private static Stream<Arguments> provideACTypes() {
    return Stream.of(
        Arguments.of("StillImage", "Still_Image"),
        Arguments.of("Image", "Still_Image"),
        Arguments.of("Sound", "Sound"),
        Arguments.of("MovingImage", "Moving_Image"),
        Arguments.of("RandomString", null),
        Arguments.of(null, null)
    );
  }

  private static Stream<Arguments> provideFormatTypes() {
    return Stream.of(
        Arguments.of("image/jpeg", "Still_Image"),
        Arguments.of("audio/example", "Sound"),
        Arguments.of("video/example", "Moving_Image"),
        Arguments.of("RandomString", null),
        Arguments.of(null, null)
    );
  }

  private static Stream<Arguments> provideABCDFormats() {
    return Stream.of(
        Arguments.of("image/jpeg", "Still_Image"),
        Arguments.of("APPLICATION/JSON", "Dataset"),
        Arguments.of("unknown/format", null),
        Arguments.of(null, null)
    );
  }

  @ParameterizedTest
  @MethodSource("provideACTypes")
  void testRetrieveFromDWCA(String acType, String expected) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:type", acType);

    // When
    var result = mediaType.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("provideFormatTypes")
  void testRetrieveFromDWCANoType(String format, String expected) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.putNull("dcterms:type");
    unit.putNull("dc:type");
    unit.put("dcterms:format", format);

    // When
    var result = mediaType.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("provideABCDFormats")
  void testRetrieveFromABCD(String format, String expected) {
    // Given
    var unit = MAPPER.createObjectNode();
    if (format != null) {
      unit.put("abcd:format", format);
    }

    // When
    var result = mediaType.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testGetTerm() {
    // When
    var result = mediaType.getTerm();

    // Then
    assertThat(result).isEqualTo(MediaType.TERM);
  }

}
