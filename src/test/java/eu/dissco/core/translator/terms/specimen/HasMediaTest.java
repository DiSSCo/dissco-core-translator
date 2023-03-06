package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HasMediaTest {

  private static final String MEDIA_URL = "https://archimg.mnhn.lu/Collections/Collections/ZS536.JPG";

  private final HasMedia hasMedia = new HasMedia();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var archiveField = new ArchiveField(0, DwcTerm.associatedMedia);
    given(archiveFile.getField("dwc:associatedMedia")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(MEDIA_URL);

    // When
    var result = hasMedia.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo("true");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:multiMediaObjects/multiMediaObject/0/fileURI", MEDIA_URL);

    // When
    var result = hasMedia.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("true");
  }

  @Test
  void testGetTerm() {
    // When
    var result = hasMedia.getTerm();

    // Then
    assertThat(result).isEqualTo(HasMedia.TERM);
  }

}
