package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.terms.Term;

public class OrganisationName extends Term {

  public static final String TERM = ODS_PREFIX + "organisationName";

  @Override
  public String getTerm() {
    return TERM;
  }
}
