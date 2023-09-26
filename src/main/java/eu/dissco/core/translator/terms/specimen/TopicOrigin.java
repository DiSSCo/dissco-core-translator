package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.schema.DigitalSpecimen.OdsTopicOrigin;
import eu.dissco.core.translator.terms.Term;

public class TopicOrigin extends Term {

  public static final String TERM = ODS_PREFIX + "topicOrigin";

  public OdsTopicOrigin calculate(eu.dissco.core.translator.schema.DigitalSpecimen ds) {
    return OdsTopicOrigin.NATURAL;
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
