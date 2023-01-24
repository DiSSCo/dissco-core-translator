package eu.dissco.core.translator.terms;

import com.fasterxml.jackson.databind.JsonNode;
import efg.DataSets.DataSet;
import eu.dissco.core.translator.component.MappingComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TermMapper {

  private final MappingComponent mapping;

  public String retrieveFromDWCA(Term term, ArchiveFile archiveFile, Record rec) {
    var harmonisedTerm = term.getTerm();
    if (mapping.getDefaultMappings().containsKey(harmonisedTerm)) {
      return mapping.getDefaultMappings().get(harmonisedTerm);
    }
    if (mapping.getFieldMappings().containsKey(harmonisedTerm)) {
      return rec.value(
          archiveFile.getField(mapping.getFieldMappings().get(harmonisedTerm)).getTerm());
    }
    return term.retrieveFromDWCA(archiveFile, rec);
  }

  public String retrieveFromABCD(Term term, JsonNode unit) {
    var termName = term.getTerm();
    if (mapping.getDefaultMappings().containsKey(termName)) {
      return mapping.getDefaultMappings().get(termName);
    } else if (mapping.getFieldMappings().containsKey(termName)) {
      var value = unit.get(mapping.getFieldMappings().get(termName));
      if (value != null && value.isTextual()) {
        return value.asText();
      }
    }
    return term.retrieveFromABCD(unit);
  }

  public String retrieveFromABCD(Term term, DataSet datasets) {
    return term.retrieveFromABCD(datasets);
  }
}
