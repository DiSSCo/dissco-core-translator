package eu.dissco.core.translator.terms.media;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

@Slf4j
public class Format extends Term {

  public static final String TERM = "dcterms:format";
  private final List<String> dwcaTerms = List.of(TERM, "dc:format");
  private final List<String> abcdTerms = List.of("abcd:format");


  @Override
  public String retrieveFromDWCA(ArchiveFile archiveFile, Record rec) {
    return searchDWCAForTerm(archiveFile, rec, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchAbcdForTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
