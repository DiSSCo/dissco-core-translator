package eu.dissco.core.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.schema.DigitalMedia;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.core.io.ClassPathResource;

public class TestUtils {

  public static ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();
  public static String SOURCE_SYSTEM_ID = "20.5000.1025/GW0-TYL-YRU";
  public static String ENDPOINT = "https://data.rbge.org.uk/service/dwca/data/darwin_core_living.zip";

  public static String INSTITUTION_ID = "https://ror.org/02y22ws83";
  public static String NORMALISED_PHYSICAL_SPECIMEN_ID = "http://coldb.mnhn.fr/catalognumber/mnhn/ec/ec10867";

  public static UUID JOB_ID = UUID.fromString("4a9be957-b8f6-4467-a98e-d19cbd2fa6ec");

  public static String MOCK_DATE = "29-09-2023";
  public static Map<String, String> DEFAULT_MAPPING = Map.of(
      "ods:physicalSpecimenIdType", "cetaf",
      "ods:type", "ZoologyVertebrateSpecimen",
      "ods:organisationId", INSTITUTION_ID
  );
  public static Map<String, String> TERM_MAPPING = Map.of(
      "ods:physicalSpecimenId", "dwc:occurrenceID",
      "ods:specimenName", "dwc:scientificName",
      "ods:physicalSpecimenCollection", "dwc:collectionID",
      "ods:datasetId", "dwc:datasetID"
  );
  public static String MAPPING_JSON = """
      {
        "mapping": [
          {
            "ods:physicalSpecimenId": "dwc:occurrenceID"
          },
          {
            "ods:specimenName": "dwc:scientificName"
          },
          {
            "ods:physicalSpecimenCollection": "dwc:collectionID"
          },
          {
            "ods:datasetId": "dwc:datasetID"
          }
        ],
        "defaults": [
          {
            "ods:physicalSpecimenIdType": "cetaf"
          },
          {
            "ods:type": "ZoologyVertebrateSpecimen"
          },
          {
            "ods:organisationId": "https://ror.org/02y22ws83"
          }
        ]
      }""";

  public static String loadResourceFile(String fileName) throws IOException {
    return new String(new ClassPathResource(fileName).getInputStream()
        .readAllBytes(), StandardCharsets.UTF_8);
  }

  public static DigitalSpecimen givenDigitalSpecimen() {
    return new DigitalSpecimen()
        .withOdsNormalisedPhysicalSpecimenID(NORMALISED_PHYSICAL_SPECIMEN_ID)
        .withDwcInstitutionID(INSTITUTION_ID);
  }

  public static DigitalMedia givenDigitalMedia() {
    return new DigitalMedia().withAcAccessURI("https://accessuri.eu/image_1");
  }

  public static Stream<Arguments> provideInvalidDigitalSpecimen() {
    return Stream.of(
        Arguments.of(new DigitalSpecimen().withOdsNormalisedPhysicalSpecimenID(
            NORMALISED_PHYSICAL_SPECIMEN_ID)),
        Arguments.of(new DigitalSpecimen().withDwcInstitutionID(INSTITUTION_ID))
    );
  }
}
