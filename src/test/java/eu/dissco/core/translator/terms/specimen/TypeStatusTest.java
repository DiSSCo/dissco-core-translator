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
class TypeStatusTest {

  private static final String STATUS = "holotype | FULL_NAME | CITATION";

  private final TypeStatus typeStatus = new TypeStatus();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var archiveField = new ArchiveField(0, DwcTerm.scientificName);
    given(archiveFile.getField("dwc:typeStatus")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(STATUS);

    // When
    var result = typeStatus.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(STATUS);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put(
        "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typeStatus",
        "holotype");
    unit.put(
        "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typifiedName/fullScientificNameString",
        "FULL_NAME");
    unit.put(
        "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/nomenclaturalReference/titleCitation",
        "CITATION");
    unit.put(
        "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/1/typeStatus",
        "Another_Status");

    // When
    var result = typeStatus.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(STATUS);
  }

  @Test
  void testGetTerm() {
    // When
    var result = typeStatus.getTerm();

    // Then
    assertThat(result).isEqualTo(TypeStatus.TERM);
  }

}
