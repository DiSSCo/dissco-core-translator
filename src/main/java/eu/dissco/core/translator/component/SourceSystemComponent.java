package eu.dissco.core.translator.component;

import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.SourceSystemRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;


@Component
public class SourceSystemComponent {

  private final WebClientProperties webClientProperties;
  private final SourceSystemRepository repository;
  @Getter
  private final String sourceSystemID;
  @Getter
  private final String sourceSystemName;
  @Getter
  private final String sourceSystemEndpoint;

  public SourceSystemComponent(WebClientProperties webClientProperties,
      SourceSystemRepository repository) {
    this.webClientProperties = webClientProperties;
    this.repository = repository;

    this.sourceSystemID = webClientProperties.getSourceSystemId();
    var sourceSystemInformation = repository.getSourceSystem(this.sourceSystemID);
    if (sourceSystemInformation == null) {
      throw new IllegalArgumentException("Source System Identifier: " + sourceSystemID + " not found");
    }
    this.sourceSystemName = sourceSystemInformation.sourceSystemName();
    this.sourceSystemEndpoint = sourceSystemInformation.sourceSystemUrl();
  }
}
