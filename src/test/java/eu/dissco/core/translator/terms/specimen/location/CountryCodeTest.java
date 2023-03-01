package eu.dissco.core.translator.terms.specimen.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.terms.specimen.ObjectType;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountryCodeTest {

  private static final String COUNTRY_CODE = "DEU";

  private final CountryCode countryCode = new CountryCode();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var archiveField = new ArchiveField(0, DwcTerm.countryCode);
    given(archiveFile.getField("dwc:countryCode")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(COUNTRY_CODE);

    // When
    var result = countryCode.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(COUNTRY_CODE);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:gathering/country/iso3166Code", COUNTRY_CODE);

    // When
    var result = countryCode.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(COUNTRY_CODE);
  }

  @Test
  void testGetTerm() {
    // When
    var result = countryCode.getTerm();

    // Then
    assertThat(result).isEqualTo(CountryCode.TERM);
  }

}
