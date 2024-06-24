package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.terms.Term;

public class SpecimenName extends Term {

  public static final String TERM = ODS_PREFIX + "specimenName";

  public String calculate(DigitalSpecimen ds) {
    var acceptedIdentification = retrieveAcceptedIdentification(ds);
    if (acceptedIdentification != null && acceptedIdentification.getOdsHasTaxonIdentification() != null
        && !acceptedIdentification.getOdsHasTaxonIdentification().isEmpty()) {
      if (acceptedIdentification.getOdsHasTaxonIdentification().get(0)
          .getDwcScientificName() != null) {
        return acceptedIdentification.getOdsHasTaxonIdentification().get(0)
            .getDwcScientificName();
      }
      if (acceptedIdentification.getOdsHasTaxonIdentification().get(0)
          .getDwcVernacularName() != null) {
        return acceptedIdentification.getOdsHasTaxonIdentification().get(0)
            .getDwcVernacularName();
      }
    }
    return null;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
