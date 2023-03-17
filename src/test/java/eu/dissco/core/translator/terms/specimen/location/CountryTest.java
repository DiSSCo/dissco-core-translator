package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountryTest {

  private static final String COUNTRY = "Germany";

  private final Country country = new Country();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:country", COUNTRY);

    // When
    var result = country.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(COUNTRY);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
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
