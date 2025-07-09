package eu.dissco.core.translator.terms.specimen.assertion;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class MeasurementTypeIRI extends Term {

  public static final String TERM = "dwciri:measurementTypeID";

  private final List<String> dwcaTerms = List.of(
      "http://rs.iobis.org/obis/terms/measurementTypeID");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
