package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.terms.Term;

public class SpecimenName extends Term {

  public static final String TERM = ODS_PREFIX + "specimenName";

  public String calculate(eu.dissco.core.translator.schema.DigitalSpecimen ds) {
    var acceptedIdentification = retrieveAcceptedIdentification(ds);
    if (acceptedIdentification != null && acceptedIdentification.getTaxonIdentifications() != null
        && !acceptedIdentification.getTaxonIdentifications().isEmpty()) {
      return acceptedIdentification.getTaxonIdentifications().get(0).getDwcScientificName();
    }
    return null;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
