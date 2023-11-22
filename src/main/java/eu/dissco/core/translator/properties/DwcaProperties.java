package eu.dissco.core.translator.properties;

import eu.dissco.core.translator.Profiles;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@Profile(Profiles.DWCA)
@ConfigurationProperties(prefix = "dwca")
public class DwcaProperties {

  @NotBlank
  private String downloadFile;

  @NotBlank
  private String tempFolder;

}
