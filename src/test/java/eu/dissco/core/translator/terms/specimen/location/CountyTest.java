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
class CountyTest {

  private final County county = new County();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var countyString = "Northumberland";
    var archiveField = new ArchiveField(0, DwcTerm.county);
    given(archiveFile.getField("dwc:county")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(countyString);

    // When
    var result = county.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(countyString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = county.getTerm();

    // Then
    assertThat(result).isEqualTo(County.TERM);
  }

}
