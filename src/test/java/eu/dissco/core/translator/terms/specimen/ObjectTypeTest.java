package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DcTerm;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ObjectTypeTest {

  private final ObjectType objectType = new ObjectType();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var objectTypeString = "alcohol jar";
    var archiveField = new ArchiveField(0, DwcTerm.preparations);
    given(archiveFile.getField("dwc:preparations")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(objectTypeString);

    // When
    var result = objectType.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(objectTypeString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var objectTypeString = "alcohol jar";
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:kindOfUnit/0/value", objectTypeString);

    // When
    var result = objectType.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(objectTypeString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = objectType.getTerm();

    // Then
    assertThat(result).isEqualTo(ObjectType.TERM);
  }

}
