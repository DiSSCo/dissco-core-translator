package eu.dissco.core.translator.component;

import eu.dissco.core.translator.exception.DisscoRepositoryException;
import eu.dissco.core.translator.properties.ApplicationProperties;
import eu.dissco.core.translator.repository.DataMappingRepository;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataMappingComponent {

  private final ApplicationProperties properties;
  private final DataMappingRepository repository;

  @Getter
  private final Map<String, String> fieldMappings = new HashMap<>();
  @Getter
  private final Map<String, String> defaults = new HashMap<>();

  @PostConstruct
  void setup() throws DisscoRepositoryException {
    var objectNode = repository.retrieveMapping(properties.getSourceSystemId());
    var mappingObject = objectNode.get("ods:hasTermMapping");
    if (mappingObject != null) {
      mappingObject.iterator().forEachRemaining(node -> node.fields()
          .forEachRemaining(field -> fieldMappings.put(field.getKey(), field.getValue().asText())));
    } else {
      log.info("No term mappings found for source system {}", properties.getSourceSystemId());
    }
    var defaultObject = objectNode.get("ods:hasDefaultMapping");
    if (defaultObject != null) {
      defaultObject.iterator().forEachRemaining(node -> node.fields()
          .forEachRemaining(field -> defaults.put(field.getKey(), field.getValue().asText())));
    } else {
      log.info("No default mappings found for source system {}", properties.getSourceSystemId());
    }
  }

}
