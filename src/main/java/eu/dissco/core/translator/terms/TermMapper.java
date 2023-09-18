package eu.dissco.core.translator.terms;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.component.MappingComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TermMapper {

  private final MappingComponent mapping;

  public String retrieveTerm(Term term, JsonNode unit, boolean dwc) {
    var termName = term.getTerm();
    if (mapping.getDefaultMappings().containsKey(termName)) {
      return mapping.getDefaultMappings().get(termName);
    } else if (mapping.getFieldMappings().containsKey(termName)) {
      var value = unit.get(mapping.getFieldMappings().get(termName));
      if (value != null && value.isTextual()) {
        return value.asText();
      }
    }
    if (dwc) {
      return term.retrieveFromDWCA(unit);
    } else {
      return term.retrieveFromABCD(unit);
    }
  }

}
