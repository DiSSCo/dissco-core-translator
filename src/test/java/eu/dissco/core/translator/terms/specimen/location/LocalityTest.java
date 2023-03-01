package eu.dissco.core.translator.terms.specimen.location;

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
class LocalityTest {

  private static final String LOCALITY_STRING =
      "Schnegaer Mühlenbach, Dümme-System, between Wöhningen and Brückau, Dümme-Elbe System, "
          + "Niedersachsen/Lower Saxony";

  private final Locality locality = new Locality();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var archiveField = new ArchiveField(0, DwcTerm.locality);
    given(archiveFile.getField("dwc:locality")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(LOCALITY_STRING);

    // When
    var result = locality.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(LOCALITY_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:gathering/localityText/value", LOCALITY_STRING);

    // When
    var result = locality.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(LOCALITY_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = locality.getTerm();

    // Then
    assertThat(result).isEqualTo(Locality.TERM);
  }
}
