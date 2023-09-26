package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.terms.Term;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MarkedAsType extends Term {

  public static final String TERM = ODS_PREFIX + "markedAsType";
  private static final Set<String> NOT_TYPE_STATUS = new HashSet<>(
      Arrays.asList("false", "specimen", ""));

  public Boolean calculate(eu.dissco.core.translator.schema.DigitalSpecimen ds) {
    var acceptedIdentification = retrieveAcceptedIdentification(ds);
    if (acceptedIdentification != null) {
      var dcwTypeStatus = acceptedIdentification.getDwcTypeStatus();
      if (dcwTypeStatus != null){
        return !NOT_TYPE_STATUS.contains(dcwTypeStatus.toLowerCase());
      }
    }
    return null;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
