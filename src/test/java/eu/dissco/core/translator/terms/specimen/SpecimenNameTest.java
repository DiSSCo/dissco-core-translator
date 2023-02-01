package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.terms.specimen.SpecimenName;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpecimenNameTest {

  private final SpecimenName specimenName = new SpecimenName();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var specimenNameString = "SpecimenName";
    var archiveField = new ArchiveField(0, DwcTerm.scientificName);
    given(archiveFile.getField("dwc:scientificName")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(specimenNameString);

    // When
    var result = specimenName.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(specimenNameString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var specimenNameString = "SpecimenName";
    var unit = new ObjectMapper().createObjectNode();
    unit.put(
        "abcd-efg:identifications/identification/0/result/mineralRockIdentified/classifiedName/fullScientificNameString",
        specimenNameString);

    // When
    var result = specimenName.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(specimenNameString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = specimenName.getTerm();

    // Then
    assertThat(result).isEqualTo(SpecimenName.TERM);
  }

}
