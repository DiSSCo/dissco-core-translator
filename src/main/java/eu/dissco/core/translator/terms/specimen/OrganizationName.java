package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.terms.Term;

public class OrganizationName extends Term {

  public static final String TERM = ODS_PREFIX + "organizationName";

  @Override
  public String getTerm() {
    return TERM;
  }
}
