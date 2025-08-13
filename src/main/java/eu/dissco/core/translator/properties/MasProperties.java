package eu.dissco.core.translator.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("mas")
public class MasProperties {

  @NotNull
  List<String> additionalSpecimenMass = List.of();
  @NotNull
  List<String> additionalMediaMass = List.of();
  @NotBlank
  Boolean forceMasSchedule = false;

}
