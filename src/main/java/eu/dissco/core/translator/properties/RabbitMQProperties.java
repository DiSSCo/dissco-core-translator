package eu.dissco.core.translator.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "rabbitmq")
public class RabbitMQProperties {

  @NotBlank
  private String exchangeName = "nu-search-exchange";

  @NotNull
  private String routingKeyName = "nu-search";

}
