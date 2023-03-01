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
class IslandTest {

  private final Island island = new Island();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var islandString = "Texel";
    var archiveField = new ArchiveField(0, DwcTerm.island);
    given(archiveFile.getField("dwc:island")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(islandString);

    // When
    var result = island.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(islandString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = island.getTerm();

    // Then
    assertThat(result).isEqualTo(Island.TERM);
  }
}
