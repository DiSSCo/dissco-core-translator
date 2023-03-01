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
class CountryTest {
  private static final String COUNTRY = "Germany";

  private final Country country = new Country();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var archiveField = new ArchiveField(0, DwcTerm.country);
    given(archiveFile.getField("dwc:country")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(COUNTRY);

    // When
    var result = country.retrieveFromDWCA(archiveFile, rec);

    // Then
    assertThat(result).isEqualTo(COUNTRY);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:gathering/country/name/value", COUNTRY);

    // When
    var result = country.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(COUNTRY);
  }

  @Test
  void testGetTerm() {
    // When
    var result = country.getTerm();

    // Then
    assertThat(result).isEqualTo(Country.TERM);
  }
}
