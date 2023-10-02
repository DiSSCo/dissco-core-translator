package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.schema.DigitalSpecimen.OdsTopicDomain;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class TopicDomain extends Term {

  public static final String TERM = ODS_PREFIX + "topicDomain";
  private static final List<String> FOSSIL_BASIS_OF_RECORD = List.of("FOSSILSPECIMEN",
      "FOSSIL SPECIMEN", "FOSSIL", "PRESERVEDSPECIMEN", "PRESERVED SPECIMEN", "LIVING SPECIMEN",
      "LIVINGSPECIMEN");
  private static final List<String> EXTRATERRESTRIAL_BASIS_OF_RECORD = List.of("METEORITE",
      "METEORITESPECIMEN", "METEORITE SPECIMEN");
  private static final List<String> EARTH_SYSTEM_BASIS_OF_RECORD = List.of("ROCK", "MINERAL",
      "ROCKSPECIMEN", "ROCK SPECIMEN", "MINERALSPECIMEN", "MINERAL SPECIMEN");

  public OdsTopicDomain calculate(DigitalSpecimen ds) {
    var basisOfRecord = ds.getDwcBasisOfRecord();
    if (basisOfRecord != null) {
      var harBasisOfRecord = basisOfRecord.trim().toUpperCase();
      if (FOSSIL_BASIS_OF_RECORD.contains(harBasisOfRecord)) {
        return OdsTopicDomain.LIFE;
      } else if (EXTRATERRESTRIAL_BASIS_OF_RECORD.contains(harBasisOfRecord)) {
        return OdsTopicDomain.EXTRATERRESTRIAL;
      } else if (EARTH_SYSTEM_BASIS_OF_RECORD.contains(harBasisOfRecord)) {
        return OdsTopicDomain.EARTH_SYSTEM;
      } else {
        return OdsTopicDomain.UNCLASSIFIED;
      }
    } else {
      return OdsTopicDomain.UNCLASSIFIED;
    }
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
