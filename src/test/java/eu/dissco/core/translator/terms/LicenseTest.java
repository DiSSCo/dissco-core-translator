package eu.dissco.core.translator.terms;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import efg.ContentMetadata;
import efg.DataSets.DataSet;
import efg.IPRStatements;
import efg.IPRStatements.Licenses;
import efg.Statement;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DcTerm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LicenseTest {

  private final License license = new License();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

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
    var archiveField = new ArchiveField(0, DcTerm.license);
    given(archiveFile.getField("dcterms:license")).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(licenseString);

    // When
    var result = license.retrieveFromDWCA(archiveFile, rec);

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
