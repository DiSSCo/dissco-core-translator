package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

public class DateCollected extends Term {

  public static final String TERM = ODS_PREFIX + "dateCollected";

  private final List<String> dwcaTerms = List.of("dwc:eventDate");
  private final List<String> abcdTerms = List.of("abcd:gathering/dateTime/isodateTimeStart",
      "abcd:gathering/dateTime/isodateTimeEnd", "abcd:gathering/dateTime/dateText");

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
