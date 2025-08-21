package eu.dissco.core.translator.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("mas")
public class MasProperties {

  @NotNull
  Set<String> specimenMass = Set.of();
  @NotNull
  Set<String> mediaMass = Set.of();
  @NotBlank
  Boolean forceMasSchedule = false;

}
