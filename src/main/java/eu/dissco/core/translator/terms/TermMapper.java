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
    if (mapping.getDefaults().containsKey(termName)) {
      return mapping.getDefaults().get(termName);
    } else if (mapping.getFieldMappings().containsKey(termName)) {
      var value = unit.get(mapping.getFieldMappings().get(termName));
      if (value != null && value.isTextual()) {
        return value.asText();
      } else {
        log.info("No value for the specific field mapping for term: {} with mapping: {}",
            term.getTerm(), value);
      }
    }
    if (dwc) {
      return term.retrieveFromDWCA(unit);
    } else {
      return term.retrieveFromABCD(unit);
    }
  }

}
