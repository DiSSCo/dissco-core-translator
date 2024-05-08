package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class RecordedById extends Term {

  public static final String TERM = DWC_PREFIX + "recordedByID";

  private final List<String> dwcaTerms = List.of("dwc:recordedByID");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
