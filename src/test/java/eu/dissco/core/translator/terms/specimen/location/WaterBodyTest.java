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
class WaterBodyTest {

  private final WaterBody waterBody = new WaterBody();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var waterBodyString = "The Aegean Sea";
    var archiveField = new ArchiveField(0, DwcTerm.waterBody);
    given(archiveFile.getField("dwc:waterBody")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(waterBodyString);

    // When
    var result = waterBody.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(waterBodyString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = waterBody.getTerm();

    // Then
    assertThat(result).isEqualTo(WaterBody.TERM);
  }

}
