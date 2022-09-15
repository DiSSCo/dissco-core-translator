package eu.dissco.core.translator.properties;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "dwca")
public class DwcaProperties {

  @NotBlank
  private String downloadFile;

  @NotBlank
  private String tempFolder;

}
