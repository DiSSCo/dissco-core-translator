package eu.dissco.core.translator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;

public class TestUtils {

  public static final Map<String, String> FIELD_MAPPING = Map.of("specimen_name",
      "abcd:identifications/identification/0/result/taxonIdentified/scientificName/fullScientificNameString",
      "physical_specimen_id", "abcd:unitID");
  public static final Map<String, String> DEFAULTS = Map.of("organization_id",
      "https://ror.org/0443cwa12", "type", "ZoologyVertebrateSpecimen",
      "physical_specimen_id_type", "cetaf");

  public static String loadResourceFile(String fileName) throws IOException {
    return new String(new ClassPathResource(fileName).getInputStream()
        .readAllBytes(), StandardCharsets.UTF_8);
  }

}
