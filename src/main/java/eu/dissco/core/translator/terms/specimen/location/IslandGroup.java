package eu.dissco.core.translator.terms.specimen.location;

import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

public class IslandGroup extends Term {
  public static final String TERM = DWC_PREFIX + "islandGroup";
  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(ArchiveFile archiveFile, Record rec) {
    return super.searchDWCAForTerm(archiveFile, rec, dwcaTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
