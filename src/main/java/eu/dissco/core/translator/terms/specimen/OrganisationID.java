package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.terms.Term;

public class OrganisationID extends Term {

  public static final String TERM = ODS_PREFIX + "organisationID";

  @Override
  public String getTerm() {
    return TERM;
  }
}
