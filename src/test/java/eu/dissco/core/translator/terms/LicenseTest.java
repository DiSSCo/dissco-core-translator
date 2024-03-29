package eu.dissco.core.translator.terms;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LicenseTest {

  private static final String LICENSE_STRING = "https://creativecommons.org/licenses/by-nc/4.0";

  private final License license = new License();

  @ParameterizedTest
  @ValueSource(strings = {"dcterms:license", "eml:license"})
  void testRetrieveFromDWCA(String term) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(term, LICENSE_STRING);

    // When
    var result = license.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(LICENSE_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:iprstatements/licenses/license/0/uri", LICENSE_STRING);
    unit.put("abcd:metadata/iprstatements/licenses/license/0/uri", "Another License");

    // When
    var result = license.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(LICENSE_STRING);
  }

  @Test
  void testRetrieveFromABCDMetadata() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:iprstatements/licenses/license/0/text", "Another License");

    // When
    var result = license.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("Another License");
  }

  @Test
  void testGetTerm() {
    // When
    var result = license.getTerm();

    // Then
    assertThat(result).isEqualTo(License.TERM);
  }

}
