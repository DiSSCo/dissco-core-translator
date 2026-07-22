package eu.dissco.core.translator.terms.specimen;

import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;

@Slf4j
public class LivingOrPreserved extends Term {

  public static final String TERM = ODS_PREFIX + "livingOrPreserved";

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return determineLiving(new BasisOfRecord().retrieveFromDWCA(unit));
  }

  private String determineLiving(String basisOfRecord) {
    if (basisOfRecord == null) {
      log.warn("Null basis of record. Unable to determine livingOrPreserved");
      return null;
    }
    if (basisOfRecord.toUpperCase().strip().equals("LIVINGSPECIMEN")) {
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
