package eu.dissco.core.translator.terms.media;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.schema.Assertion;
import eu.dissco.core.translator.terms.Term;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class MediaAssertions {

  private final List<Pair<Term, String>> abcdMediaAssertions = List.of(
      Pair.of(new FileSize(), "kb"),
      Pair.of(new PixelXDimension(), "pixel"), Pair.of(new PixelYDimension(), "pixel"));

  public List<Assertion> gatherAssertions(JsonNode data,
      boolean dwc) {
    if (!dwc) {
      return gatherEventAssertionsForABCD(data);
    }
    return List.of();
  }

  private List<Assertion> gatherEventAssertionsForABCD(
      JsonNode data) {
    var assertions = new ArrayList<Assertion>();
    for (var abcdMediaAssertion : abcdMediaAssertions) {
      var value = abcdMediaAssertion.getLeft().retrieveFromABCD(data);
      if (value != null) {
        var assertion = new Assertion()
            .withDwcMeasurementValue(abcdMediaAssertion.getLeft().retrieveFromABCD(data))
            .withDwcMeasurementType(abcdMediaAssertion.getLeft().getTerm())
            .withDwcMeasurementUnit(abcdMediaAssertion.getRight());
        assertions.add(assertion);
      }
    }
    return assertions;
  }
}
