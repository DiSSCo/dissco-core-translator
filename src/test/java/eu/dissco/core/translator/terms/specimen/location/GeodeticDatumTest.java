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
class GeodeticDatumTest {

  private static final String GEODETIC_DATUM_STRING = "WGS84";

  private final GeodeticDatum geodeticDatum = new GeodeticDatum();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var archiveField = new ArchiveField(0, DwcTerm.geodeticDatum);
    given(archiveFile.getField("dwc:geodeticDatum")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(GEODETIC_DATUM_STRING);

    // When
    var result = geodeticDatum.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(GEODETIC_DATUM_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put(
        "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/spatialDatum",
        GEODETIC_DATUM_STRING);

    // When
    var result = geodeticDatum.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(GEODETIC_DATUM_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = geodeticDatum.getTerm();

    // Then
    assertThat(result).isEqualTo(GeodeticDatum.TERM);
  }

}
