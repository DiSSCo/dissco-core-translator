package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.terms.Term;

public class SpecimenName extends Term {

  public static final String TERM = ODS_PREFIX + "specimenName";

  public String calculate(DigitalSpecimen ds) {
    var acceptedIdentification = retrieveAcceptedIdentification(ds);
    if (acceptedIdentification != null && acceptedIdentification.getTaxonIdentifications() != null
        && !acceptedIdentification.getTaxonIdentifications().isEmpty()) {
      if (acceptedIdentification.getTaxonIdentifications().get(0)
          .getDwcScientificName() != null){
        return acceptedIdentification.getTaxonIdentifications().get(0)
            .getDwcScientificName();
      }
      if (acceptedIdentification.getTaxonIdentifications().get(0)
          .getDwcVernacularName() != null) {
        return acceptedIdentification.getTaxonIdentifications().get(0)
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
