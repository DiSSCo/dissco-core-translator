package eu.dissco.core.translator.terms;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class License extends Term {

  public static final String TERM = DCTERMS_PREFIX + "license";
  private final List<String> dwcaTerms = List.of(TERM, "dc:license", "eml:license");
  private final List<String> abcdUnitTerms = List.of(
      "abcd:iprstatements/licenses/license/0/uri",
      "abcd:iprstatements/licenses/license/0/text",
      "abcd:ipr/licenses/license/0/uri",
      "abcd:ipr/licenses/license/0/text");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return searchJsonForTerm(unit, abcdUnitTerms);
  }
}
