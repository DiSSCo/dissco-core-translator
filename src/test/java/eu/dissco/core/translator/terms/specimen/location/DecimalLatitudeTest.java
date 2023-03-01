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
class DecimalLatitudeTest {
  private static final String LATITUDE_STRING = "53.33167";

  private final DecimalLatitude decimalLatitude = new DecimalLatitude();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var archiveField = new ArchiveField(0, DwcTerm.decimalLatitude);
    given(archiveFile.getField("dwc:decimalLatitude")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(LATITUDE_STRING);

    // When
    var result = decimalLatitude.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(LATITUDE_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put(
        "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/latitudeDecimal",
        LATITUDE_STRING);

    // When
    var result = decimalLatitude.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(LATITUDE_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = decimalLatitude.getTerm();

    // Then
    assertThat(result).isEqualTo(DecimalLatitude.TERM);
  }
}
