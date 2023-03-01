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
class IslandGroupTest {

  private final IslandGroup islandGroup = new IslandGroup();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var islandGroupString = "Harrison Islands";
    var archiveField = new ArchiveField(0, DwcTerm.islandGroup);
    given(archiveFile.getField("dwc:islandGroup")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(islandGroupString);

    // When
    var result = islandGroup.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(islandGroupString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = islandGroup.getTerm();

    // Then
    assertThat(result).isEqualTo(IslandGroup.TERM);
  }
}
