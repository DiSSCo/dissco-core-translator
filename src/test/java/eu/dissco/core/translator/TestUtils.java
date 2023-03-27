package eu.dissco.core.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;

public class TestUtils {

  public static ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

  public static final Map<String, String> ABCD_DEFAULTS = Map.of(
      "ods:organisationId", "https://ror.org/0443cwa12",
      "ods:type", "ZoologyVertebrateSpecimen",
      "ods:physicalSpecimenIdType", "cetaf");
  public static final Map<String, String> DWC_DEFAULTS = Map.of(
      "ods:organisationId", "https://ror.org/02y22ws83",
      "ods:type", "ZoologyVertebrateSpecimen",
      "ods:physicalSpecimenIdType", "cetaf");
  public static final Map<String, String> DWC_KEW_DEFAULTS = Map.of(
      "ods:organisationId", "https://ror.org/00ynnr806",
      "ods:type", "ZoologyVertebrateSpecimen",
      "ods:physicalSpecimenIdType", "cetaf");

  public static String SOURCE_SYSTEM_ID = "20.5000.1025/GW0-TYL-YRU";
  public static String ENDPOINT = "https://data.rbge.org.uk/service/dwca/data/darwin_core_living.zip";
  public static Map<String, String> DEFAULT_MAPPING = Map.of(
      "ods:physicalSpecimenIdType", "cetaf",
      "ods:type", "ZoologyVertebrateSpecimen",
      "ods:organisationId", "https://ror.org/02y22ws83"
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

}
