package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;

public class LivingSpecimen extends Term {

  public static final String TERM = ODS_PREFIX + "livingSpecimen";

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return determineLiving(new BasisOfRecord().retrieveFromDWCA(unit));
  }

  private String determineLiving(String basisOfRecord) {
    if(basisOfRecord.toUpperCase().strip().equals("LIVINGSPECIMEN")){
      return "Living";
    } else {
      return "Preserved";
    }
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return determineLiving(new BasisOfRecord().retrieveFromABCD(unit));
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
