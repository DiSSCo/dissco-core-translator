package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

public class TypeStatus extends Term {

  public static final String TERM = ODS_PREFIX + "typeStatus";

  private final List<String> dwcaTerms = List.of("dwc:typeStatus");
  // Pick the first TypeStatus from ABCD
  private final List<String> abcdTerms = List.of("abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typeStatus");

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
