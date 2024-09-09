package eu.dissco.core.translator.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "application")
public class ApplicationProperties {

  @NotBlank
  private String sourceSystemId;

  private int itemsPerRequest = 100;

  @Positive
  private Integer maxItems;

}
