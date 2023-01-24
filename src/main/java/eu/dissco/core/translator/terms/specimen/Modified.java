package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.time.Instant;
import java.util.List;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

public class Modified extends Term {
  public static final String TERM = ODS_PREFIX + "modified";
  private final List<String> dwcaTerms = List.of("dcterms:modified");
  private final List<String> abcdTerms = List.of("abcd:hasDateModified", "abcd:dateLastEdited");

  @Override
  public String retrieveFromDWCA(ArchiveFile archiveFile, Record rec) {
    return super.searchDWCAForTerm(archiveFile, rec, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var epochString = super.searchAbcdForTerm(unit, abcdTerms);
    if (epochString != null){
      var timestamp = Instant.ofEpochMilli(Long.parseLong(epochString));
      return timestamp.toString();
    }
    return null;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
