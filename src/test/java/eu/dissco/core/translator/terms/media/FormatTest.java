package eu.dissco.core.translator.terms.media;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DcElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FormatTest {

  private final Format format = new Format();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var formatString = "StillImage";
    var archiveField = new ArchiveField(0, DcElement.format);
    given(archiveFile.getField("dcterms:format")).willReturn(null);
    given(archiveFile.getField("dc:format")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(formatString);

    // When
    var result = format.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(formatString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var formatString = "SpecimenName";
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:format", formatString);

    // When
    var result = format.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(formatString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = format.getTerm();

    // Then
    assertThat(result).isEqualTo(Format.TERM);
  }

}
