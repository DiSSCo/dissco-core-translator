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
class StateProvinceTest {

  private final StateProvince stateProvince = new StateProvince();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var stateProvinceString = "Brittany";
    var archiveField = new ArchiveField(0, DwcTerm.stateProvince);
    given(archiveFile.getField("dwc:stateProvince")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(stateProvinceString);

    // When
    var result = stateProvince.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(stateProvinceString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = stateProvince.getTerm();

    // Then
    assertThat(result).isEqualTo(StateProvince.TERM);
  }
}
