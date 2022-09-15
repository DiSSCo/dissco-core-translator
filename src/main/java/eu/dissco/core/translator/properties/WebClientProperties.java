package eu.dissco.core.translator.properties;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "webclient")
public class WebClientProperties {

  @NotBlank
  private String sourceSystemId;

  private String queryParams;

  private int itemsPerRequest = 500;

  private String contentNamespace;

}
