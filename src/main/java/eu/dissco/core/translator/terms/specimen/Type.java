package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.terms.Term;

public class Type extends Term {

  public static final String TERM = ODS_PREFIX + "type";

  @Override
  public String getTerm() {
    return TERM;
  }
}
