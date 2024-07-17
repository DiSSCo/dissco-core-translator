package eu.dissco.core.translator.component;

import eu.dissco.core.translator.exception.DisscoRepositoryException;
import eu.dissco.core.translator.properties.WebClientProperties;
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

  private final WebClientProperties properties;
  private final DataMappingRepository repository;

  @Getter
  private final Map<String, String> fieldMappings = new HashMap<>();
  @Getter
  private final Map<String, String> defaults = new HashMap<>();

  @PostConstruct
  void setup() throws DisscoRepositoryException {
    var objectNode = repository.retrieveMapping(properties.getSourceSystemId());
    var mappingObject = objectNode.get("ods:FieldMapping");
    if (mappingObject != null) {
      mappingObject.iterator().forEachRemaining(node -> node.fields()
          .forEachRemaining(field -> fieldMappings.put(field.getKey(), field.getValue().asText())));
    }
    var defaultObject = objectNode.get("ods:DefaultMapping");
    defaultObject.iterator().forEachRemaining(node -> node.fields()
        .forEachRemaining(field -> defaults.put(field.getKey(), field.getValue().asText())));
  }

}
