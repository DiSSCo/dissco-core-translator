package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.terms.Term;

public class OrganisationId extends Term {

  public static final String TERM = ODS_PREFIX + "organisationId";

  @Override
  public String getTerm() {
    return TERM;
  }
}
