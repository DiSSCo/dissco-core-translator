package eu.dissco.core.translator.terms.media;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.Stream;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.AcTerm;
import org.gbif.dwc.terms.DcElement;
import org.gbif.dwc.terms.DcTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MediaTypeTest {

  private final MediaType mediaType = new MediaType();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @ParameterizedTest
  @MethodSource("provideACTypes")
  void testRetrieveFromDWCA(String acType, String expected) {
    // Given
    var archiveField = new ArchiveField(0, DcTerm.type);
    given(archiveFile.getField("dcterms:type")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(acType);

    // When
    var result = mediaType.retrieveFromDWCA(archiveFile, rec);

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
    var formatField = new ArchiveField(0, DcElement.format);
    given(archiveFile.getField("dcterms:type")).willReturn(null);
    given(archiveFile.getField("dc:type")).willReturn(null);
    given(archiveFile.getField("dcterms:format")).willReturn(formatField);
    given(rec.value(formatField.getTerm())).willReturn(format);

    // When
    var result = mediaType.retrieveFromDWCA(archiveFile, rec);

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
