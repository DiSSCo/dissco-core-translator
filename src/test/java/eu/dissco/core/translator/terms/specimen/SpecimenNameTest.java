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
  void testRetrieveFromABCDEFG() {
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
  void testRetrieveFromABCDWithPreferred() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put(
        "abcd:identifications/identification/0/preferredFlag", false);
    unit.put("abcd:identifications/identification/0/result/taxonIdentified/scientificName/fullScientificNameString",
        "Arrabidaea chica (Humb. & Bonpl.) B.Verl.");
    unit.put(
        "abcd:identifications/identification/1/preferredFlag", true);
    unit.put("abcd:identifications/identification/1/result/taxonIdentified/scientificName/fullScientificNameString",
        "Bignonia chica Humb. & Bonpl.");
    unit.put(
        "abcd:identifications/identification/2/preferredFlag", false);
    unit.put("abcd:identifications/identification/2/result/taxonIdentified/scientificName/fullScientificNameString",
        "Arrabidaea chica var. thyrsoidea Bureau");

    // When
    var result = specimenName.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("Bignonia chica Humb. & Bonpl.");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var specimenNameString = "SpecimenName";
    var unit = new ObjectMapper().createObjectNode();
    unit.put(
        "abcd:identifications/identification/0/result/taxonIdentified/scientificName/fullScientificNameString",
        specimenNameString);

    // When
    var result = specimenName.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(specimenNameString);
  }

  @Test
  void testRetrieveFromABCDNotPresent() {
    // Given
    var unit = new ObjectMapper().createObjectNode();

    // When
    var result = specimenName.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }


  @Test
  void testGetTerm() {
    // When
    var result = specimenName.getTerm();

    // Then
    assertThat(result).isEqualTo(SpecimenName.TERM);
  }

}
