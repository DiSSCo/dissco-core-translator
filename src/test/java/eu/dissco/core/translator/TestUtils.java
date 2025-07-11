package eu.dissco.core.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.domain.SourceSystemInformation;
import eu.dissco.core.translator.schema.DigitalMedia;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.core.io.ClassPathResource;

public class TestUtils {

  public static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();
  public static final String SOME_VALUE = "someValue";
  public static final String SOURCE_SYSTEM_ID = "20.5000.1025/GW0-TYL-YRU";
  public static final String SOURCE_SYSTEM_NAME = "Naturalis Biodiversity Center (NL) - Vermes";
  public static final String ENDPOINT = "https://data.rbge.org.uk/service/dwca/data/darwin_core_living.zip";

  public static final String ORGANISATION_ID = "https://ror.org/02y22ws83";
  public static final String NORMALISED_PHYSICAL_SPECIMEN_ID = "http://coldb.mnhn.fr/catalognumber/mnhn/ec/ec10867";

  public static final UUID JOB_ID = UUID.fromString("4a9be957-b8f6-4467-a98e-d19cbd2fa6ec");

  public static final String MOCK_DATE = "29-09-2023";
  public static final Map<String, String> DEFAULT_MAPPING = Map.of(
      "ods:physicalSpecimenIDType", "cetaf",
      "ods:type", "ZoologyVertebrateSpecimen",
      "ods:organisationID", ORGANISATION_ID
  );
  public static final Map<String, String> TERM_MAPPING = Map.of(
      "ods:physicalSpecimenID", "dwc:occurrenceID",
      "ods:specimenName", "dwc:scientificName",
      "ods:physicalSpecimenCollection", "dwc:collectionID",
      "ods:datasetID", "dwc:datasetID"
  );
  public static final String MAPPING_JSON = """
      {
         "@id": "https://hdl.handle.net/TEST/PH1-C7E-Q4J",
         "@type": "ods:DataMapping",
         "schema:identifier": "https://hdl.handle.net/TEST/PH1-C7E-Q4J",
         "ods:fdoType": "https://hdl.handle.net/21.T11148/ce794a6f4df42eb7e77e",
         "ods:status": "ods:Active",
         "schema:version": 2,
         "schema:name": "RBINS default mapping",
         "schema:description": "The default mapping for RBINS datasets",
         "schema:dateCreated": "2024-07-17T09:06:47.707Z",
         "schema:dateModified": "2024-07-17T09:30:28.513Z",
         "schema:creator": {
             "@id": "adf294ba-bb03-4962-8042-a37f1648458e",
             "@type": "schema:Person",
             "ods:hasIdentifier": []
         },
        "ods:hasTermMapping": [
          {
            "ods:physicalSpecimenID": "dwc:occurrenceID"
          },
          {
            "ods:specimenName": "dwc:scientificName"
          },
          {
            "ods:physicalSpecimenCollection": "dwc:collectionID"
          },
          {
            "ods:datasetID": "dwc:datasetID"
          }
        ],
        "ods:hasDefaultMapping": [
          {
            "ods:physicalSpecimenIDType": "cetaf"
          },
          {
            "ods:type": "ZoologyVertebrateSpecimen"
          },
          {
            "ods:organisationID": "https://ror.org/02y22ws83"
          }
        ],
        "ods:mappingDataStandard": "DwC"
      }""";

  public static String loadResourceFile(String fileName) throws IOException {
    return new String(new ClassPathResource(fileName).getInputStream()
        .readAllBytes(), StandardCharsets.UTF_8);
  }

  public static DigitalSpecimen givenDigitalSpecimen() {
    return new DigitalSpecimen()
        .withOdsNormalisedPhysicalSpecimenID(NORMALISED_PHYSICAL_SPECIMEN_ID)
        .withOdsOrganisationID(ORGANISATION_ID);
  }

  public static DigitalMedia givenDigitalMedia() {
    return new DigitalMedia().withAcAccessURI("https://accessuri.eu/image_1");
  }

  public static Stream<Arguments> provideInvalidDigitalSpecimen() {
    return Stream.of(
        Arguments.of(new DigitalSpecimen().withOdsNormalisedPhysicalSpecimenID(
            NORMALISED_PHYSICAL_SPECIMEN_ID)),
        Arguments.of(new DigitalSpecimen().withOdsOrganisationID(ORGANISATION_ID))
    );
  }

  public static SourceSystemInformation givenSourceSystemInformation() {
    return new SourceSystemInformation(SOURCE_SYSTEM_NAME, ENDPOINT, List.of());
  }
}
