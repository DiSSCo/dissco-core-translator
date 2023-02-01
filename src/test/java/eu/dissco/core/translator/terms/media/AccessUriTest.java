package eu.dissco.core.translator.terms.media;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.AcTerm;
import org.gbif.dwc.terms.DcElement;
import org.gbif.dwc.terms.DcTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccessUriTest {

  private final AccessUri accessUri = new AccessUri();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var accessUriString = "https://accessuri.eu/image_1";
    var archiveField = new ArchiveField(0, AcTerm.accessURI);
    given(archiveFile.getField("ac:accessURI")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(accessUriString);

    // When
    var result = accessUri.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(accessUriString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var accessUriString = "https://accessuri.eu/image_1";
    var unit = new ObjectMapper().createObjectNode();
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
    assertThat(result).isEqualTo(AccessUri.TERM);
  }

}
