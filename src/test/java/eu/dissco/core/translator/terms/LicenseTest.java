package eu.dissco.core.translator.terms;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import efg.ContentMetadata;
import efg.DataSets.DataSet;
import efg.IPRStatements;
import efg.IPRStatements.Licenses;
import efg.Statement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LicenseTest {

  private static final String LICENSE_STRING = "https://creativecommons.org/licenses/by-nc/4.0";

  private final License license = new License();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:license", LICENSE_STRING);

    // When
    var result = license.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(LICENSE_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var dataset = MAPPER.createObjectNode();
    dataset.put("abcd:metadata/iprstatements/licenses/license/0/uri", "Another License");
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:iprstatements/licenses/license/0/uri", LICENSE_STRING);

    // When
    var result = license.retrieveFromABCD(dataset, unit);

    // Then
    assertThat(result).isEqualTo(LICENSE_STRING);
  }

  @Test
  void testRetrieveFromABCDMetadata() {
    // Given
    var dataset = MAPPER.createObjectNode();
    dataset.put("abcd:metadata/iprstatements/licenses/license/0/text", "Another License");
    var unit = MAPPER.createObjectNode();

    // When
    var result = license.retrieveFromABCD(dataset, unit);

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
