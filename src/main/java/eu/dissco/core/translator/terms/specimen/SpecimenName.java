package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

@Slf4j
public class SpecimenName extends Term {

  public static final String TERM = ODS_PREFIX + "specimenName";
  private final List<String> dwcaTerms = List.of("dwc:scientificName");
  private final Pair<String, String> abcdTerm = Pair.of(
      "abcd:identifications/identification/%s/preferredFlag",
      "abcd:identifications/identification/%s/result/taxonIdentified/scientificName/fullScientificNameString");

  private final Pair<String, String> abcdefgTerm = Pair.of(
      "abcd-efg:identifications/identification/%s/preferredFlag",
      "abcd-efg:identifications/identification/%s/result/mineralRockIdentified/classifiedName/fullScientificNameString");

  @Override
  public String retrieveFromDWCA(ArchiveFile archiveFile, Record rec) {
    return super.searchDWCAForTerm(archiveFile, rec, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var result = getSpecimenName(unit, abcdTerm);
    if (result == null){
      result = getSpecimenName(unit, abcdefgTerm);
    }
    return result;
  }

  private String getSpecimenName(JsonNode unit, Pair<String, String> term) {
    var iterateOverElements = true;
    var numberFound = 0;
    while (iterateOverElements) {
      if (unit.get(String.format(term.getLeft(), numberFound)) != null) {
        var preffered = unit.get(String.format(term.getLeft(), numberFound)).asBoolean();
        if (preffered) {
          if (unit.get(String.format(term.getRight(), numberFound)) != null) {
            return unit.get(String.format(term.getRight(), numberFound)).asText();
          }
        } else {
          numberFound++;
        }
      } else {
        if (unit.get(String.format(term.getRight(), 0)) != null) {
          return unit.get(String.format(term.getRight(), 0)).asText();
        } else {
          iterateOverElements = false;
        }
      }
    }
    return null;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
