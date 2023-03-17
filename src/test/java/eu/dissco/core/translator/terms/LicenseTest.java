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

  private final License license = new License();

  private static DataSet getDataSet(String licenseString, boolean useUri) {
    var dataset = new DataSet();
    var metadata = new ContentMetadata();
    var ipr = new IPRStatements();
    var licenseElement = new Licenses();
    var statement = new Statement();
    if (useUri) {
      statement.setURI(licenseString);
    } else {
      statement.setText(licenseString);
    }
    licenseElement.getLicense().add(statement);
    ipr.setLicenses(licenseElement);
    metadata.setIPRStatements(ipr);
    dataset.setMetadata(metadata);
    return dataset;
  }

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var licenseString = "https://creativecommons.org/licenses/by-nc/4.0";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:license", licenseString);

    // When
    var result = license.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(licenseString);
  }

  @Test
  void testRetrieveFromABCDUri() {
    // Given
    var licenseString = "https://creativecommons.org/licenses/by-nc/4.0";
    DataSet dataset = getDataSet(licenseString, true);

    // When
    var result = license.retrieveFromABCD(dataset);

    // Then
    assertThat(result).isEqualTo(licenseString);
  }

  @Test
  void testRetrieveFromABCDText() {
    // Given
    var licenseString = "https://creativecommons.org/licenses/by-nc/4.0";
    DataSet dataset = getDataSet(licenseString, false);

    // When
    var result = license.retrieveFromABCD(dataset);

    // Then
    assertThat(result).isEqualTo(licenseString);
  }

  @Test
  void testRetrieveFromABCDEmpty() {
    // Given
    DataSet dataset = new DataSet();

    // When
    var result = license.retrieveFromABCD(dataset);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = license.getTerm();

    // Then
    assertThat(result).isEqualTo(License.TERM);
  }

}
