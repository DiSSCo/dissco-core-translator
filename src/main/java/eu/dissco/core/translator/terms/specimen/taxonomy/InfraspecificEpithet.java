package eu.dissco.core.translator.terms.specimen.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

public class InfraspecificEpithet extends AbstractTaxonomy {


  public static final String TERM = DWC_PREFIX + "infraspecificEpithet";

  private final List<String> dwcaTerms = List.of(TERM);

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.getTaxonFromDWCA(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    return super.searchABCDTerms(unit, List.of("/nameAtomised/botanical/infraspecificEpithet"));
  }

  @Override
  public String getTerm() {
    return TERM;
  }

}