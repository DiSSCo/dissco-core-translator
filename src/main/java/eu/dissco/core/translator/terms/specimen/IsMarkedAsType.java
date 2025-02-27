package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.terms.Term;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class IsMarkedAsType extends Term {

  public static final String TERM = ODS_PREFIX + "isMarkedAsType";
  private static final Set<String> NOT_TYPE_STATUS = new HashSet<>(
      Arrays.asList("false", "specimen", ""));

  public Boolean calculate(DigitalSpecimen ds) {
    var acceptedIdentification = retrieveAcceptedIdentification(ds);
    if (acceptedIdentification != null) {
      var dcwTypeStatus = acceptedIdentification.getDwcTypeStatus();
      if (dcwTypeStatus != null) {
        return !NOT_TYPE_STATUS.contains(dcwTypeStatus.trim().toLowerCase());
      }
    }
    return null;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
