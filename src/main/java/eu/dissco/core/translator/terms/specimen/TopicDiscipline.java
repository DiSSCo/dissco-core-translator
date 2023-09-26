package eu.dissco.core.translator.terms.specimen;

import eu.dissco.core.translator.schema.DigitalSpecimen.OdsTopicDiscipline;
import eu.dissco.core.translator.terms.Term;
import java.util.List;

public class TopicDiscipline extends Term {

  public static final String TERM = ODS_PREFIX + "topicDiscipline";

  private static final List<String> FOSSIL_BASIS_OF_RECORD = List.of("FOSSILSPECIMEN",
      "FOSSIL SPECIMEN", "FOSSIL");
  private static final List<String> EXTRATERRESTRIAL_BASIS_OF_RECORD = List.of("METEORITE",
      "METEORITESPECIMEN", "METEORITE SPECIMEN");
  private static final List<String> EARTH_SYSTEM_BASIS_OF_RECORD = List.of("ROCK", "MINERAL",
      "ROCKSPECIMEN", "ROCK SPECIMEN", "MINERALSPECIMEN", "MINERAL SPECIMEN");

  public OdsTopicDiscipline calculate(eu.dissco.core.translator.schema.DigitalSpecimen ds) {
    var basisOfRecord = ds.getDwcBasisOfRecord();
    var acceptedIdentification = retrieveAcceptedIdentification(ds);
    if (acceptedIdentification != null && acceptedIdentification.getTaxonIdentifications() != null
        && !acceptedIdentification.getTaxonIdentifications().isEmpty()) {
      return getDiscipline(basisOfRecord,
          acceptedIdentification.getTaxonIdentifications().get(0).getDwcKingdom());
    }
    return getDiscipline(basisOfRecord, null);
  }

  private OdsTopicDiscipline getDiscipline(String basisOfRecord, String kingdom) {
    if (basisOfRecord != null) {
      var harBasisOfRecord = basisOfRecord.trim().toUpperCase();
      if (FOSSIL_BASIS_OF_RECORD.contains(harBasisOfRecord)) {
        return OdsTopicDiscipline.PALAEONTOLOGY;
      } else if (EXTRATERRESTRIAL_BASIS_OF_RECORD.contains(harBasisOfRecord)) {
        return OdsTopicDiscipline.ASTROGEOLOGY;
      } else if (EARTH_SYSTEM_BASIS_OF_RECORD.contains(harBasisOfRecord)) {
        return OdsTopicDiscipline.EARTH_GEOLOGY;
      } else if (kingdom != null) {
        var harmonisedKingdom = kingdom.trim().toUpperCase();
        switch (harmonisedKingdom) {
          case "ANIMALIA" -> {
            return OdsTopicDiscipline.ZOOLOGY;
          }
          case "PLANTAE" -> {
            return OdsTopicDiscipline.BOTANY;
          }
          case "BACTERIA" -> {
            return OdsTopicDiscipline.MICROBIOLOGY;
          }
          default -> {
            return OdsTopicDiscipline.UNCLASSIFIED;
          }
        }
      } else {
        return OdsTopicDiscipline.UNCLASSIFIED;
      }
    } else {
      return OdsTopicDiscipline.UNCLASSIFIED;
    }
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
