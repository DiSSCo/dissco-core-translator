package eu.dissco.core.translator.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

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
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Stream;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

class EmlComponentTest {

  private MockedStatic<Clock> clockMock;
  private MockedStatic<UUID> uuidMock;

  private static Stream<Arguments> generateDataset() throws DatatypeConfigurationException {
    return Stream.of(
        Arguments.of(generateFullDataset(),
            "<eml xmlns=\"https://eml.ecoinformatics.org/eml-2.2.0\" packageId=\"123e4567-e89b-12d3-a456-426614174000\" system=\"https://dissco.eu\" xml:lang=\"en\"><dataset xmlns=\"\"><title>Dataflos</title><pubDate>2025-06-25</pubDate><language>en</language><abstract>This is a test dataset</abstract><contact><individualName><surname>Sam</surname></individualName><address><deliveryPoint>123 Test Street</deliveryPoint></address><electronicMailAddress>test@test.com</electronicMailAddress><phone>+123456789</phone></contact><associatedParty><individualName><surname>Sam</surname></individualName><role>TECHNICAL_POINT_OF_CONTACT</role></associatedParty><intellectualRights>Only be used for testing purposes</intellectualRights><licensed><licenseName>CC-BY-4.0</licenseName><url>https://creativecommons.org/licenses/by/4.0/</url></licensed><distribution><online><url>http://example.com/dataset</url></online></distribution><coverage><geographicCoverage><geographicDescription>Global</geographicDescription><boundingCoordinates><westBoundingCoordinate>-180</westBoundingCoordinate><eastBoundingCoordinate>180</eastBoundingCoordinate><northBoundingCoordinate>-90</northBoundingCoordinate><southBoundingCoordinate>90</southBoundingCoordinate></boundingCoordinates></geographicCoverage></coverage></dataset></eml>"),
        Arguments.of(generateDataSetWithEmptyLists(),
            "<eml xmlns=\"https://eml.ecoinformatics.org/eml-2.2.0\" packageId=\"123e4567-e89b-12d3-a456-426614174000\" system=\"https://dissco.eu\" xml:lang=\"en\"><dataset xmlns=\"\"><title>Dataset with empty lists</title></dataset></eml>"),
        Arguments.of(generateMinimalDataset(),
            "<eml xmlns=\"https://eml.ecoinformatics.org/eml-2.2.0\" packageId=\"123e4567-e89b-12d3-a456-426614174000\" system=\"https://dissco.eu\" xml:lang=\"en\"><dataset xmlns=\"\"><title>Minimal Dataset</title><intellectualRights>Only be used for testing purposes</intellectualRights></dataset></eml>")
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
    return dataset;
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
    return dataset;
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

  @BeforeEach
  void setup() {
    mockInstant(1750843566); // set desired return value 2021-12-20T11:33:20Z
    mockUUID();
  }

  @AfterEach
  void destroy() {
    clockMock.close();
    uuidMock.close();
  }

  @MethodSource("generateDataset")
  @ParameterizedTest
  void testGenerateEml(DataSets.DataSet dataset, String expected) throws IOException {
    // Given

    // When
    var result = EmlComponent.generateEML(dataset);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  private void mockInstant(long expected) {
    Clock spyClock = spy(Clock.systemDefaultZone());
    clockMock = mockStatic(Clock.class);
    clockMock.when(Clock::systemDefaultZone).thenReturn(spyClock);
    when(spyClock.instant()).thenReturn(Instant.ofEpochSecond(expected));
  }

  private void mockUUID() {
    var uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    uuidMock = mockStatic(UUID.class);
    uuidMock.when(UUID::randomUUID).thenReturn(uuid);
  }

}
