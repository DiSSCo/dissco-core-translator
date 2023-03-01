package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

public class CollectingNumber extends Term {

  public static final String TERM = ODS_PREFIX + "collectingNumber";

  private final List<String> dwcaTerms = List.of("dwc:recordNumber");
  private final List<String> abcdTerms = List.of("abcd:collectorsFieldNumber", "abcd:gathering/code");

  @Override
  public String retrieveFromDWCA(ArchiveFile archiveFile, Record rec) {
    return super.searchDWCAForTerm(archiveFile, rec, dwcaTerms);
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
