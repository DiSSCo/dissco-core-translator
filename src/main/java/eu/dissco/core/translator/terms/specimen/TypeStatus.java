package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;

public class TypeStatus extends Term {

  public static final String TERM = DWC_PREFIX + "typeStatus";

  private final List<String> dwcaTerms = List.of(TERM);

  // Pick the first TypeStatus from ABCD
  private final List<String> abcdTermsTypeStatus =
      List.of(
          "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typeStatus");
  private final List<String> abcdTermsTypeName =
      List.of(
          "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typifiedName/fullScientificNameString");
  private final List<String> abcdTermsCitation =
      List.of(
          "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/nomenclaturalReference/titleCitation");

  @Override
  public String retrieveFromDWCA(ArchiveFile archiveFile, Record rec) {
    return super.searchDWCAForTerm(archiveFile, rec, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var status = super.searchAbcdForTerm(unit, abcdTermsTypeStatus);
    var typeName = super.searchAbcdForTerm(unit, abcdTermsTypeName);
    var citation = super.searchAbcdForTerm(unit, abcdTermsCitation);
    return status + " | " + typeName + " | " + citation;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
