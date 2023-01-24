package eu.dissco.core.translator.terms;


import com.fasterxml.jackson.databind.JsonNode;

import efg.DataSets.DataSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

@Slf4j
public abstract class Term {

  protected static final String ODS_PREFIX = "ods:";

  public String retrieveFromDWCA(ArchiveFile archiveFile, Record rec) {
    log.info("No specific attributes retrieve specified for term: {}", getTerm());
    return "";
  }

  protected String searchDWCAForTerm(ArchiveFile archiveFile, Record rec, List<String> originalTerms){
    for (var originalTerm : originalTerms) {
      if (archiveFile.getField(originalTerm) != null){
        return rec.value(archiveFile.getField(originalTerm).getTerm());
      }
    }
    log.info("Term not found in any of these search fields: {}", originalTerms);
    return null;
  }

  protected String searchAbcdForTerm(JsonNode attributes, List<String> originalTerms){
    for (var originalTerm : originalTerms) {
      if (attributes.get(originalTerm) != null){
        return attributes.get(originalTerm).asText();
      }
    }
    log.info("Term not found in any of these search fields: {}", originalTerms);
    return null;
  }

  public abstract String getTerm();

  public String retrieveFromABCD(JsonNode unit) {
    log.info("No specific attributes retrieve specified for field: {}", getTerm());
    return "";
  }

  public String retrieveFromABCD(DataSet datasets) {
    log.info("No specific attributes retrieve specified for field: {}", getTerm());
    return "";
  }
}
