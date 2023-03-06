package eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

public class LatestAgeOrHighestStage extends AbstractChronoStratigraphy {

  public static final String TERM = DWC_PREFIX + "latestAgeOrHighestStage";
  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(ArchiveFile archiveFile, Record rec) {
    return super.searchDWCAForTerm(archiveFile, rec, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchABCDChronostratigraphy(unit, List.of( "Stage"));
  }

  @Override
  public String getTerm() {
    return TERM;
  }

}
