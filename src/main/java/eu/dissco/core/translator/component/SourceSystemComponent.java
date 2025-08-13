package eu.dissco.core.translator.component;

import eu.dissco.core.translator.properties.ApplicationProperties;
import eu.dissco.core.translator.repository.SourceSystemRepository;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SourceSystemComponent {

  private final String sourceSystemID;
  private final String sourceSystemName;
  private final String sourceSystemEndpoint;
  private final String sourceSystemFilters;
  private final List<String> specimenMass;
  private final List<String> mediaMass;
  private final SourceSystemRepository repository;

  public SourceSystemComponent(ApplicationProperties applicationProperties,
      SourceSystemRepository repository) {
    this.sourceSystemID = applicationProperties.getSourceSystemId();
    this.repository = repository;
    var sourceSystemInformation = repository.getSourceSystem(this.sourceSystemID);
    if (sourceSystemInformation == null) {
      throw new IllegalArgumentException(
          "Source System Identifier: " + sourceSystemID + " not found");
    }
    this.sourceSystemName = sourceSystemInformation.sourceSystemName();
    this.sourceSystemEndpoint = sourceSystemInformation.sourceSystemUrl();
    if (!sourceSystemInformation.sourceSystemFilters().isEmpty()) {
      var stringBuilder = new StringBuilder();
      sourceSystemInformation.sourceSystemFilters().forEach(stringBuilder::append);
      this.sourceSystemFilters = stringBuilder.toString();
    } else {
      sourceSystemFilters = "";
    }
    this.specimenMass = sourceSystemInformation.specimenMass();
    this.mediaMass = sourceSystemInformation.mediaMass();
  }

  public void storeEmlRecord(File emlFile) throws IOException {
    repository.storeEml(Files.readAllBytes(emlFile.toPath()), sourceSystemID);
  }

  public void storeEmlRecord(String eml) {
    repository.storeEml(eml.getBytes(StandardCharsets.UTF_8), sourceSystemID);
  }
}
