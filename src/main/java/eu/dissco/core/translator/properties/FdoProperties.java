package eu.dissco.core.translator.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "fdo")
public class FdoProperties {

  @NotBlank
  private String digitalSpecimenType;

  @NotBlank
  private String digitalMediaType;

  @NotBlank
  private String applicationName = "DiSSCo Translator Service";

  @NotBlank
  private String applicationPID = "https://doi.org/10.5281/zenodo.14379776";

}
