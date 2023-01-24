package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhysicalSpecimenIdTest {

  private final PhysicalSpecimenId physicalSpecimenId = new PhysicalSpecimenId();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var id = "123456789";
    var archiveField = new ArchiveField(0, DwcTerm.occurrenceID);
    given(archiveFile.getField("dwc:occurrenceID")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(id);

    // When
    var result = physicalSpecimenId.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(id);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var id = "123456789";
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:unitID", id);

    // When
    var result = physicalSpecimenId.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(id);
  }

  @Test
  void testGetTerm() {
    // When
    var result = physicalSpecimenId.getTerm();

    // Then
    assertThat(result).isEqualTo(PhysicalSpecimenId.TERM);
  }

}
