package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.terms.Term;

public class OrganizationId extends Term {

  public static final String TERM = ODS_PREFIX + "organizationId";

  @Override
  public String getTerm() {
    return TERM;
  }
}
