package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import eu.dissco.core.translator.terms.License;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.gbif.dwc.terms.DwcaTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataSetIdTest {

  private final DatasetId datasetId = new DatasetId();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var datasetIdString = "datasetId-123456";
    var archiveField = new ArchiveField(0, DwcTerm.datasetID);
    given(archiveFile.getField("dwc:datasetID")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(datasetIdString);

    // When
    var result = datasetId.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(datasetIdString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = datasetId.getTerm();

    // Then
    assertThat(result).isEqualTo(DatasetId.TERM);
  }
}
