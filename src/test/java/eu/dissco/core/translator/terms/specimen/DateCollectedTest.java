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
class DateCollectedTest {

  private static final String DATE = "1924-11-20";

  private final DateCollected dateCollected = new DateCollected();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var archiveField = new ArchiveField(0, DwcTerm.scientificName);
    given(archiveFile.getField("dwc:eventDate")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(DATE);

    // When
    var result = dateCollected.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(DATE);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:gathering/dateTime/isodateTimeStart", DATE);

    // When
    var result = dateCollected.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(DATE);
  }

  @Test
  void testGetTerm() {
    // When
    var result = dateCollected.getTerm();

    // Then
    assertThat(result).isEqualTo(DateCollected.TERM);
  }

}
