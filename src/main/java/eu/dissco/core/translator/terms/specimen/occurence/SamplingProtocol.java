package eu.dissco.core.translator.terms.specimen.occurence;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class SamplingProtocol extends Term {

  public static final String TERM = DWC_PREFIX + "samplingProtocol";

  private final List<String> abcdTerms = List.of("abcd:gathering/method/value");
  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchJsonForTerm(unit, abcdTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }

}
