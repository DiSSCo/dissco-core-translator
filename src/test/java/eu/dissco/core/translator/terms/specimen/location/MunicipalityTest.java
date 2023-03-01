package eu.dissco.core.translator.terms.specimen.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MunicipalityTest {

  private final Municipality municipality = new Municipality();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var municipalityString = "Gouda";
    var archiveField = new ArchiveField(0, DwcTerm.municipality);
    given(archiveFile.getField("dwc:municipality")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(municipalityString);

    // When
    var result = municipality.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(municipalityString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = municipality.getTerm();

    // Then
    assertThat(result).isEqualTo(Municipality.TERM);
  }

}
