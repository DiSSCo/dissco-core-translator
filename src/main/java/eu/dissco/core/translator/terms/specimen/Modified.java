package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.configuration.ApplicationConfiguration.DATE_STRING;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Modified extends Term {

  public static final String TERM = "dcterms:modified";
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_STRING).withZone(
      ZoneOffset.UTC);
  private final List<String> dwcaTerms = List.of(TERM);
  private final List<String> abcdUnitTerms = List.of("abcd:hasDateModified", "abcd:dateLastEdited",
      "abcd:revisionData/dateModified");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var modified = super.searchJsonForTerm(unit, dwcaTerms);
    return evaluateDate(modified);
  }

  private String evaluateDate(String valueInData) {
    if (valueInData != null && !valueInData.isBlank()) {
      return valueInData;
    }
    return formatter.format(Instant.now());
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var modified = searchJsonForTerm(unit, abcdUnitTerms);
    return evaluateDate(modified);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
