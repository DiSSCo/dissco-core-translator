package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

public class HasMedia extends Term {

  public static final String TERM = ODS_PREFIX + "hasMedia";
  private final List<String> dwcaTerms = List.of("dwc:associatedMedia");
  private final List<String> abcdTerms = List.of("abcd:multiMediaObjects/multiMediaObject/0/fileURI");

  @Override
  public String retrieveFromDWCA(ArchiveFile archiveFile, Record rec) {
    var result = super.searchDWCAForTerm(archiveFile, rec, dwcaTerms);
    return toBoolean(result);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var result = super.searchAbcdForTerm(unit, abcdTerms);
    return toBoolean(result);
  }

  private static String toBoolean(String result) {
    return String.valueOf(result != null);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
