package eu.dissco.core.translator.component;

import static org.assertj.core.api.Assertions.assertThat;

import efg.ContentMetadata;
import efg.ContentMetadata.Description;
import efg.DataSets;
import efg.DataSets.DataSet.ContentContacts;
import efg.IPRStatements.Copyrights;
import efg.IPRStatements.Licenses;
import efg.IPRStatements.TermsOfUseStatements;
import efg.MetadataDescriptionRepr;
import efg.MicroAgentP;
import efg.Statement;
import java.io.IOException;
import java.time.Instant;
import java.util.stream.Stream;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class EmlComponentTest {


  @MethodSource("generateDataset")
  @ParameterizedTest
  void testGenerateEml(DataSets.DataSet dataset, String expected) throws IOException {
    // When
    var result = EmlComponent.generateEML(dataset);

    // Then
    assertThat(result).contains(expected);
  }

  private static Stream<Arguments> generateDataset() throws DatatypeConfigurationException {
    return Stream.of(
        Arguments.of(generateFullDataset(), "This is a test dataset"),
        Arguments.of(generateDataSetWithEmptyLists(), "Dataset with empty lists"),
        Arguments.of(generateMinimalDataset(), "Minimal Dataset")
    );
  }

  private static DataSets.DataSet generateDataSetWithEmptyLists() {
    var dataset = new DataSets.DataSet();
    var metadata = new ContentMetadata();
    var description = new Description();
    var representation = new MetadataDescriptionRepr();
    representation.setTitle("Dataset with empty lists");
    description.getRepresentation().add(representation);
    metadata.setDescription(description);
    dataset.setMetadata(metadata);
    var licenses = new Licenses();
    licenses.getLicense();
    var iprStatements = new efg.IPRStatements();
    var termsOfUseStatements = new TermsOfUseStatements();
    termsOfUseStatements.getTermsOfUse();
    iprStatements.setTermsOfUseStatements(termsOfUseStatements);
    var copyRights = new Copyrights();
    copyRights.getCopyright();
    iprStatements.setCopyrights(copyRights);
    iprStatements.setLicenses(licenses);
    metadata.setIPRStatements(iprStatements);
    var contentContacts = new ContentContacts();
    contentContacts.getContentContact();
    var technicalContacts = new efg.DataSets.DataSet.TechnicalContacts();
    technicalContacts.getTechnicalContact();
    dataset.setTechnicalContacts(technicalContacts);
    dataset.setContentContacts(contentContacts);
    return  dataset;
  }

  private static DataSets.DataSet generateMinimalDataset() {
    var dataset = new DataSets.DataSet();
    var metadata = new ContentMetadata();
    var description = new Description();
    var representation = new MetadataDescriptionRepr();
    representation.setTitle("Minimal Dataset");
    description.getRepresentation().add(representation);
    metadata.setDescription(description);
    dataset.setMetadata(metadata);
    var iprStatements = new efg.IPRStatements();
    var termsOfUseStatements = new TermsOfUseStatements();
    var statement = new Statement();
    statement.setText("Only be used for testing purposes");
    termsOfUseStatements.getTermsOfUse().add(statement);
    iprStatements.setTermsOfUseStatements(termsOfUseStatements);
    metadata.setIPRStatements(iprStatements);
    return  dataset;
  }

  private static DataSets.DataSet generateFullDataset() throws DatatypeConfigurationException {
    var dataset = new DataSets.DataSet();
    var metadata = new ContentMetadata();
    var description = new Description();
    var representation = new MetadataDescriptionRepr();
    representation.setTitle("Dataflos");
    representation.setDetails("This is a test dataset");
    representation.setCoverage("Global");
    representation.setLanguage("en");
    representation.setURI("http://example.com/dataset");
    description.getRepresentation().add(representation);
    metadata.setDescription(description);
    var iprStatements = new efg.IPRStatements();
    var license = new Statement();
    license.setText("CC-BY-4.0");
    license.setURI("https://creativecommons.org/licenses/by/4.0/");
    var licenses = new Licenses();
    licenses.getLicense().add(license);
    iprStatements.setLicenses(licenses);
    var copyRights = new Copyrights();
    var copyRight = new Statement();
    copyRight.setText("Only be used for testing purposes");
    copyRights.getCopyright().add(copyRight);
    iprStatements.setCopyrights(copyRights);
    metadata.setIPRStatements(iprStatements);
    var revisionData = new efg.RevisionData();
    var date
        = DatatypeFactory.newInstance().newXMLGregorianCalendar(Instant.now().toString());
    revisionData.setDateModified(date);
    metadata.setRevisionData(revisionData);
    dataset.setMetadata(metadata);
    var contentContacts = new ContentContacts();
    var contentAgent = new MicroAgentP();
    contentAgent.setName("Sam");
    contentAgent.setAddress("123 Test Street");
    contentAgent.setPhone("+123456789");
    contentAgent.setEmail("test@test.com");
    contentContacts.getContentContact().add(contentAgent);
    var technicalContacts = new efg.DataSets.DataSet.TechnicalContacts();
    var technicalAgent = new MicroAgentP();
    technicalAgent.setName("Sam");
    technicalContacts.getTechnicalContact().add(technicalAgent);
    dataset.setTechnicalContacts(technicalContacts);
    dataset.setContentContacts(contentContacts);
    return dataset;
  }

}
