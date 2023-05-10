package eu.dissco.core.translator.terms.specimen.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

public abstract class AbstractTaxonomy extends Term {

  private static final String IDENTIFICATION = "abcd:identifications/identification/";
  private static final String IDENTIFICATION_INDEX = "ods:taxonIdentificationIndex";
  private static final Triple<String, String, String> ABCD_TAXON_RANK =
      Triple.of(
          IDENTIFICATION,
          "/result/taxonIdentified/higherTaxa/higherTaxon/",
          "/higherTaxonRank");
  private static final Triple<String, String, String> ABCD_VALUE =
      Triple.of(
          IDENTIFICATION,
          "/result/taxonIdentified/higherTaxa/higherTaxon/",
          "/higherTaxonName");
  private static final Pair<String, String> abcdPreferredFlag = Pair.of(IDENTIFICATION,
      "/preferredFlag");


  protected String getIdentificationIndex(JsonNode unit) {
    if (unit.get(IDENTIFICATION_INDEX) != null) {
      return unit.get(IDENTIFICATION_INDEX).asText();
    } else {
      var identificationIndex = determineIdentificationIndex(unit);
      ((ObjectNode) unit).put(IDENTIFICATION_INDEX, identificationIndex);
      return identificationIndex;
    }
  }

  private String determineIdentificationIndex(JsonNode unit) {
    var numberFound = 0;
    while (true) {
      if (unit.get(getStringAtIndex(abcdPreferredFlag, numberFound)) != null) {
        var optionalResult = checkPreferredFlag(unit, numberFound);
        if (optionalResult.isPresent()) {
          return optionalResult.get();
        } else {
          numberFound++;
        }
      } else {
        return "0";
      }
    }
  }

  protected String searchABCDSplitTerms(JsonNode unit, List<String> searchTerms) {
    var identificationIndex = getIdentificationIndex(unit);
    return searchABCDSplitTerms(unit, searchTerms,
        Pair.of(ABCD_TAXON_RANK.getLeft() + identificationIndex + ABCD_TAXON_RANK.getMiddle(),
            ABCD_TAXON_RANK.getRight()),
        Pair.of(ABCD_VALUE.getLeft() + identificationIndex + ABCD_VALUE.getMiddle(),
            ABCD_VALUE.getRight()));
  }

  protected String searchABCDTerms(JsonNode unit, List<String> searchTerms) {
    var identificationIndex = getIdentificationIndex(unit);
    var terms = searchTerms.stream().map(
        term -> IDENTIFICATION + identificationIndex + "/result/taxonIdentified" + term).toList();
    return searchJsonForTerm(unit, terms);
  }

  protected String getStringAtIndex(Pair<String, String> string, int numberFound) {
    return string.getLeft() + numberFound + string.getRight();
  }

  private Optional<String> checkPreferredFlag(JsonNode unit, int numberFound) {
    var preferred = unit.get(getStringAtIndex(abcdPreferredFlag, numberFound)).asBoolean();
    if (preferred) {
      return Optional.of(String.valueOf(numberFound));
    }
    return Optional.empty();
  }


}
