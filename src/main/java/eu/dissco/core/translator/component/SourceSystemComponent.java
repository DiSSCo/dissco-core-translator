package eu.dissco.core.translator.component;

import eu.dissco.core.translator.properties.ApplicationProperties;
import eu.dissco.core.translator.repository.SourceSystemRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SourceSystemComponent {

  private final String sourceSystemID;
  private final String sourceSystemName;
  private final String sourceSystemEndpoint;
  private final String sourceSystemFilters;

  public SourceSystemComponent(ApplicationProperties applicationProperties,
      SourceSystemRepository repository) {
    this.sourceSystemID = applicationProperties.getSourceSystemId();
    var sourceSystemInformation = repository.getSourceSystem(this.sourceSystemID);
    if (sourceSystemInformation == null) {
      throw new IllegalArgumentException("Source System Identifier: " + sourceSystemID + " not found");
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

  }
}
