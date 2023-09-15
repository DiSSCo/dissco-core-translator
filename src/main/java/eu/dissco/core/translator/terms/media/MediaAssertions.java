package eu.dissco.core.translator.terms.media;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class MediaAssertions {

  List<Pair<Term, String>> abcdMediaAssertions = List.of(Pair.of(new FileSize(), "kb"),
      Pair.of(new PixelXDimension(), "pixel"), Pair.of(new PixelYDimension(), "pixel"));

  public List<eu.dissco.core.translator.schema.Assertions> gatherAssertions(JsonNode data,
      boolean dwc) {
    if (dwc) {
      return gatherOccurrenceAssertionsForDwc(data);
    } else {
      return gatherOccurrenceAssertionsForABCD(data);
    }
  }

  private List<eu.dissco.core.translator.schema.Assertions> gatherOccurrenceAssertionsForABCD(
      JsonNode data) {
    var assertions = new ArrayList<eu.dissco.core.translator.schema.Assertions>();
    for (var abcdMediaAssertion : abcdMediaAssertions) {
      var value = abcdMediaAssertion.getLeft().retrieveFromABCD(data);
      if (value != null) {
        var assertion = new eu.dissco.core.translator.schema.Assertions()
            .withAssertionValue(abcdMediaAssertion.getLeft().retrieveFromABCD(data))
            .withAssertionType(abcdMediaAssertion.getLeft().getTerm())
            .withAssertionUnit(abcdMediaAssertion.getRight());
        assertions.add(assertion);
      }
    }
    return assertions;
  }

  private List<eu.dissco.core.translator.schema.Assertions> gatherOccurrenceAssertionsForDwc(
      JsonNode data) {
    return null;
  }
}
