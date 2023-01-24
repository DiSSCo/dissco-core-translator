package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

public class DatasetId extends Term {

  public static final String TERM = ODS_PREFIX + "datasetId";

  private final List<String> dwcaTerms = List.of("dwc:datasetID");

  @Override
  public String retrieveFromDWCA(ArchiveFile archiveFile, Record rec) {
    return super.searchDWCAForTerm(archiveFile, rec, dwcaTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
