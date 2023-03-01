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
class DecimalLongitudeTest {
  private static final String LONGITUDE_STRING = "10.48778";

  private final DecimalLongitude decimalLongitude = new DecimalLongitude();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var archiveField = new ArchiveField(0, DwcTerm.decimalLongitude);
    given(archiveFile.getField("dwc:decimalLongitude")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(LONGITUDE_STRING);

    // When
    var result = decimalLongitude.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(LONGITUDE_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put(
        "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/longitudeDecimal",
        LONGITUDE_STRING);

    // When
    var result = decimalLongitude.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(LONGITUDE_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = decimalLongitude.getTerm();

    // Then
    assertThat(result).isEqualTo(DecimalLongitude.TERM);
  }

}
