package eu.dissco.core.translator.terms.specimen;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import eu.dissco.core.translator.terms.specimen.taxonomy.Kingdom;
import java.util.List;

public class TopicDiscipline extends Term {

  public static final String TERM = ODS_PREFIX + "topicDiscipline";
  private static final String UNCLASSIFIED = "Unclassified";

  private static final List<String> FOSSIL_BASIS_OF_RECORD = List.of("FOSSILSPECIMEN",
      "FOSSIL SPECIMEN", "FOSSIL");
  private static final List<String> EXTRATERRESTRIAL_BASIS_OF_RECORD = List.of("METEORITE",
      "METEORITESPECIMEN", "METEORITE SPECIMEN");
  private static final List<String> EARTH_SYSTEM_BASIS_OF_RECORD = List.of("ROCK", "MINERAL",
      "ROCKSPECIMEN", "ROCK SPECIMEN", "MINERALSPECIMEN", "MINERAL SPECIMEN");

  @Override
  public String retrieveFromDWCA(JsonNode unit) {
    var basisOfRecord = new BasisOfRecord().retrieveFromDWCA(unit);
    var kingdom = new Kingdom().retrieveFromDWCA(unit);
    return getCategory(basisOfRecord, kingdom);
  }

  @Override
  public String retrieveFromABCD(JsonNode unit) {
    var basisOfRecord = new BasisOfRecord().retrieveFromABCD(unit);
    var kingdom = new Kingdom().retrieveFromABCD(unit);
    return getCategory(basisOfRecord, kingdom);
  }

  private static String getCategory(String basisOfRecord, String kingdom) {
    if (basisOfRecord != null) {
      var harBasisOfRecord = basisOfRecord.trim().toUpperCase();
      if (FOSSIL_BASIS_OF_RECORD.contains(harBasisOfRecord)) {
        return "Palaeontology";
      } else if (EXTRATERRESTRIAL_BASIS_OF_RECORD.contains(harBasisOfRecord)) {
        return "Astrogeology";
      } else if (EARTH_SYSTEM_BASIS_OF_RECORD.contains(harBasisOfRecord)){
        return "Earth System";
      } else if (kingdom != null){
        var harKingdom = kingdom.trim().toUpperCase();
        switch (harKingdom) {
          case "ANIMALIA" -> {
            return "Zoology";
          }
          case "PLANTAE" -> {
            return "Botany";
          }
          case "BACTERIA" -> {
            return "Microbiology";
          }
          default -> {
            return UNCLASSIFIED;
          }
        }
      } else {
        return UNCLASSIFIED;
      }
    } else {
      return UNCLASSIFIED;
    }
  }

  @Override
  public String getTerm() {
    return TERM;
  }
}
