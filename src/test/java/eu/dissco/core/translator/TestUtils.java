package eu.dissco.core.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;

public class TestUtils {

  public static ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

  public static final Map<String, String> ABCD_FIELD_MAPPING = Map.of(
      "specimen_name", "abcd:identifications/identification/0/result/taxonIdentified/scientificName/fullScientificNameString",
      "physical_specimen_id", "abcd:unitID");
  public static final Map<String, String> ABCD_DEFAULTS = Map.of(
      "organization_id", "https://ror.org/0443cwa12",
      "type", "ZoologyVertebrateSpecimen",
      "physical_specimen_id_type", "cetaf");

  public static final Map<String, String> DWC_FIELD_MAPPING = Map.of(
      "specimen_name", "dwc:scientificName",
      "physical_specimen_id", "dwc:occurrenceID",
      "physical_specimen_collection", "dwc:collectionID",
      "dataset_id", "dwc:datasetID"
      );
  public static final Map<String, String> DWC_DEFAULTS = Map.of(
      "organization_id", "https://ror.org/02y22ws83",
      "type", "ZoologyVertebrateSpecimen",
      "physical_specimen_id_type", "cetaf");

  public static final Map<String, String> DWC_KEW_FIELD_MAPPING = Map.of(
      "specimen_name", "dwc:scientificName",
      "physical_specimen_id", "dwc:occurrenceID",
      "physical_specimen_collection", "dwc:collectionCode");
  public static final Map<String, String> DWC_KEW_DEFAULTS = Map.of(
      "organization_id", "https://ror.org/00ynnr806",
      "type", "ZoologyVertebrateSpecimen",
      "physical_specimen_id_type", "cetaf");

  public static String SOURCE_SYSTEM_ID = "20.5000.1025/GW0-TYL-YRU";
  public static String ENDPOINT = "https://data.rbge.org.uk/service/dwca/data/darwin_core_living.zip";
  public static String MAPPING_JSON = """
      {
        "mapping": [
          {
            "physical_specimen_id": "dwc:occurrenceID"
          },
          {
            "specimen_name": "dwc:scientificName"
          },
          {
            "physical_specimen_collection": "dwc:collectionID"
          },
          {
            "dataset_id": "dwc:datasetID"
          }
        ],
        "defaults": [
          {
            "physical_specimen_id_type": "cetaf"
          },
          {
            "type": "ZoologyVertebrateSpecimen"
          },
          {
            "organization_id": "https://ror.org/02y22ws83"
          }
        ]
      }""";

  public static String loadResourceFile(String fileName) throws IOException {
    return new String(new ClassPathResource(fileName).getInputStream()
        .readAllBytes(), StandardCharsets.UTF_8);
  }

}
