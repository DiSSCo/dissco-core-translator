package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class Modified extends Term {

  public static final String TERM = "dcterms:modified";
  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdMetaTerms = List.of("abcd:metadata/revisionData/dateModified");
  private final List<String> abcdUnitTerms = List.of("abcd:hasDateModified", "abcd:dateLastEdited");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForStringTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode dataset, JsonNode unit) {
    var modified = searchJsonForStringTerm(unit, abcdUnitTerms);
    if (modified == null){
      modified = searchJsonForLongTerm(unit, abcdUnitTerms);
    }
    if (modified == null){
      modified = searchJsonForStringTerm(dataset, abcdMetaTerms);
    }
    if (modified == null){
      modified = searchJsonForLongTerm(dataset, abcdMetaTerms);
    }
    return modified;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
