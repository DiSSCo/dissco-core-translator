package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.terms.Term;

public class PhysicalSpecimenIDType extends Term {

  public static final String TERM = ODS_PREFIX + "physicalSpecimenIDType";

  @Override
  public String getTerm() {
    return TERM;
  }
}
