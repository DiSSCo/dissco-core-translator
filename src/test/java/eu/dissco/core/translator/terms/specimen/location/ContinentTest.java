package eu.dissco.core.translator.terms.specimen.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import eu.dissco.core.translator.terms.specimen.DatasetId;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContinentTest {

  private final Continent continent = new Continent();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var continentString = "Europe";
    var archiveField = new ArchiveField(0, DwcTerm.continent);
    given(archiveFile.getField("dwc:continent")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(continentString);

    // When
    var result = continent.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(continentString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = continent.getTerm();

    // Then
    assertThat(result).isEqualTo(Continent.TERM);
  }

}
