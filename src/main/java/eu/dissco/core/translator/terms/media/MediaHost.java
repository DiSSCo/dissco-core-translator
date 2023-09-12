package eu.dissco.core.translator.terms.media;

import eu.dissco.core.translator.terms.specimen.OrganisationId;

public class MediaHost extends OrganisationId {
  public static final String TERM = "mediaHost";

  @Override
  public String getTerm() {
    return TERM;
  }
}
