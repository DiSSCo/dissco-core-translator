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
class CollectingNumberTest {

  private static final String NUMBER = "245";

  private final CollectingNumber collectingNumber = new CollectingNumber();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var archiveField = new ArchiveField(0, DwcTerm.scientificName);
    given(archiveFile.getField("dwc:recordNumber")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(NUMBER);

    // When
    var result = collectingNumber.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(NUMBER);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:collectorsFieldNumber", NUMBER);

    // When
    var result = collectingNumber.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(NUMBER);
  }

  @Test
  void testGetTerm() {
    // When
    var result = collectingNumber.getTerm();

    // Then
    assertThat(result).isEqualTo(CollectingNumber.TERM);
  }

}
