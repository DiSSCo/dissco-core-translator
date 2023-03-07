package eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

public class Group extends Term {

  public static final String TERM = DWC_PREFIX + "group";

  private final List<String> dwcaTerms = List.of(TERM);
  // Take the first lithostratigraphicAttribution we can find
  private final List<String> abcdTerms = List.of(
      "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/lithostratigraphicAttributions/lithostratigraphicAttribution/0/group");

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
