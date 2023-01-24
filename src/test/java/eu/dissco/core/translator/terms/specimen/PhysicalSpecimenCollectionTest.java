package eu.dissco.core.translator.terms.specimen;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhysicalSpecimenCollectionTest {

  private final PhysicalSpecimenCollection physicalSpecimenCollection = new PhysicalSpecimenCollection();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var collectionString = "collection-123456";
    var archiveField = new ArchiveField(0, DwcTerm.collectionCode);
    given(archiveFile.getField("dwc:collectionID")).willReturn(null);
    given(archiveFile.getField("dwc:collectionCode")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(collectionString);

    // When
    var result = physicalSpecimenCollection.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(collectionString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = physicalSpecimenCollection.getTerm();

    // Then
    assertThat(result).isEqualTo(PhysicalSpecimenCollection.TERM);
  }
}
