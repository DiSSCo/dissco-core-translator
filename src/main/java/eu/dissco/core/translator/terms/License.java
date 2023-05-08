package eu.dissco.core.translator.terms;

import com.fasterxml.jackson.databind.JsonNode;
import efg.DataSets.DataSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class License extends Term {

  public static final String TERM = "dcterms:license";
  private final List<String> dwcaTerms = List.of(TERM, "dc:license");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    return super.searchJsonForTerm(unit, dwcaTerms);
  }

  @Override
  public String retrieveFromABCD(DataSet datasets) {
    if (datasets != null && datasets.getMetadata() != null
        && datasets.getMetadata().getIPRStatements() != null
        && datasets.getMetadata().getIPRStatements().getLicenses() != null
        && !datasets.getMetadata().getIPRStatements().getLicenses().getLicense().isEmpty()) {
      var license = datasets.getMetadata().getIPRStatements().getLicenses().getLicense().get(0)
          .getURI();
      if (license == null) {
        license = datasets.getMetadata().getIPRStatements().getLicenses().getLicense().get(0)
            .getText();
      }
      return license;
    }
    log.info("Unable to get license for dataset");
    return null;
  }

  @Override
  public String getTerm() {
    return TERM;
  }

}
