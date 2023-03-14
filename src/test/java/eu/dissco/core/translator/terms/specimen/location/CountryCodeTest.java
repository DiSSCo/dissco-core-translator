package eu.dissco.core.translator.terms.specimen.location;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountryCodeTest {

  private static final String COUNTRY_CODE = "DEU";

  private final CountryCode countryCode = new CountryCode();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("dwc:countryCode", COUNTRY_CODE);

    // When
    var result = countryCode.retrieveFromDWCA(unit);

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
