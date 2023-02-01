package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

@Slf4j
public class SpecimenName extends Term {

  public static final String TERM = ODS_PREFIX + "specimenName";
  private final List<String> dwcaTerms = List.of("dwc:scientificName");
  private final List<String> abcdefgTerms = List.of(
      "abcd:identifications/identification/0/result/taxonIdentified/scientificName/fullScientificNameString",
      "abcd-efg:identifications/identification/0/result/mineralRockIdentified/classifiedName/fullScientificNameString"
  );

  @Override
  public String retrieveFromDWCA(ArchiveFile archiveFile, Record rec) {
    return super.searchDWCAForTerm(archiveFile, rec, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchAbcdForTerm(unit, abcdefgTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
