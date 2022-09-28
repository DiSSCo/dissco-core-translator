package eu.dissco.core.translator.component;

import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.MappingRepository;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MappingComponent {

  private final WebClientProperties properties;
  private final MappingRepository repository;

  private final Map<String, String> fieldMappings = new HashMap<>();
  private final Map<String, String> defaults = new HashMap<>();

  @PostConstruct
  void setup() {
    var objectNode = repository.retrieveMapping(properties.getSourceSystemId());
    var mappingObject = objectNode.get("mapping");
    mappingObject.iterator().forEachRemaining(node -> node.fields()
        .forEachRemaining(field -> fieldMappings.put(field.getKey(), field.getValue().asText())));
    var defaultObject = objectNode.get("defaults");
    defaultObject.iterator().forEachRemaining(node -> node.fields()
        .forEachRemaining(field -> defaults.put(field.getKey(), field.getValue().asText())));
  }

  public Map<String, String> getFieldMappings() {
    return fieldMappings;
  }

  public Map<String, String> getDefaultMappings() {
    return defaults;
  }

}
