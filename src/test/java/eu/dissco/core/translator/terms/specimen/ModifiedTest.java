package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DcTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ModifiedTest {

  private final Modified modified = new Modified();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var licenseString = "23-03-1989";
    var archiveField = new ArchiveField(0, DcTerm.modified);
    given(archiveFile.getField("dcterms:modified")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(licenseString);

    // When
    var result = modified.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(licenseString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    String modifiedString = "1674553668909";
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:dateLastEdited", modifiedString);

    // When
    var result = modified.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(modifiedString);
  }

  @Test
  void testRetrieveFromABCDEmpty() {
    // Given
    var unit = new ObjectMapper().createObjectNode();

    // When
    var result = modified.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = modified.getTerm();

    // Then
    assertThat(result).isEqualTo(Modified.TERM);
  }

}
