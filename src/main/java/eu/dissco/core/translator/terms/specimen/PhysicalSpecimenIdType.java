package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.terms.Term;

public class PhysicalSpecimenIdType extends Term {

  public static final String TERM = ODS_PREFIX + "physicalSpecimenIdType";

  @Override
  public String getTerm() {
    return TERM;
  }
}
