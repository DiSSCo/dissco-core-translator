package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractTaxonomy extends Term {

  protected static final String IDENTIFICATION = "abcd:identifications/identification/";
  protected static final String EXTENSIONS = "extensions";
  protected static final String IDENTIFICATION_EXTENSION = "dwc:Identification";
  protected static final String IDENTIFICATION_VERIFICATION_STATUS = "dwc:identificationVerificationStatus";
  private static final String IDENTIFICATION_INDEX = "ods:taxonIdentificationIndex";
  private static final Pair<String, String> ABCD_TAXON_RANK =
      Pair.of("result/taxonIdentified/higherTaxa/higherTaxon/",
          "/higherTaxonRank");
  private static final Pair<String, String> ABCD_VALUE =
      Pair.of("result/taxonIdentified/higherTaxa/higherTaxon/",
          "/higherTaxonName");
  private static final Pair<String, String> abcdPreferredFlag = Pair.of(IDENTIFICATION,
      "/preferredFlag");

  protected String getTaxonFromDWCA(JsonNode unit, List<String> dwcaTerms) {
    var result = super.searchJsonForTerm(unit, dwcaTerms);
    if (result != null) {
      return result;
    } else if (unit.get(EXTENSIONS) != null
        && unit.get(EXTENSIONS).get(IDENTIFICATION_EXTENSION) != null) {
      var identificationIndex = getIdentificationIndexDWCA(unit);
      if (identificationIndex != null) {
        return searchJsonForTerm(
            unit.get(EXTENSIONS).get(IDENTIFICATION_EXTENSION)
                .get(Integer.parseInt(identificationIndex)), dwcaTerms);
      }
    }
    return null;
  }

  private Optional<String> determineIdentificationIndexDWCA(JsonNode unit) {
    if (unit.get(EXTENSIONS) != null
        && unit.get(EXTENSIONS).get(IDENTIFICATION_EXTENSION) != null) {
      var identifications = unit.get(EXTENSIONS).get(IDENTIFICATION_EXTENSION);
      for (int i = 0; i < identifications.size(); i++) {
        var identification = identifications.get(i);
        if (identification.get(IDENTIFICATION_VERIFICATION_STATUS) == null) {
          return Optional.of(String.valueOf(i));
        } else {
          var verificationStatus = identification.get(IDENTIFICATION_VERIFICATION_STATUS)
              .asText();
          if (verificationStatus.equals("1")) {
            return Optional.of(String.valueOf(i));
          }
        }
      }
    }
    return Optional.empty();
  }

  protected String getIdentificationIndexDWCA(JsonNode unit) {
    if (unit.get(IDENTIFICATION_INDEX) != null) {
      return unit.get(IDENTIFICATION_INDEX).asText();
    } else {
      var optionalIndex = determineIdentificationIndexDWCA(unit);
      if (optionalIndex.isPresent()) {
        ((ObjectNode) unit).put(IDENTIFICATION_INDEX, optionalIndex.get());
        return optionalIndex.get();
      } else {
        return null;
      }
    }
  }

  protected String getIdentificationIndexABCD(JsonNode unit) {
    if (unit.get(IDENTIFICATION_INDEX) != null) {
      return unit.get(IDENTIFICATION_INDEX).asText();
    } else {
      var identificationIndex = determineIdentificationIndexABCD(unit);
      ((ObjectNode) unit).put(IDENTIFICATION_INDEX, identificationIndex);
      return identificationIndex;
    }
  }

  private String determineIdentificationIndexABCD(JsonNode unit) {
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
    return searchABCDSplitTerms(unit, searchTerms,
        Pair.of(ABCD_TAXON_RANK.getLeft(), ABCD_TAXON_RANK.getRight()),
        Pair.of(ABCD_VALUE.getLeft(), ABCD_VALUE.getRight()));
  }

  protected String searchABCDTerms(JsonNode unit, List<String> searchTerms) {
    var identificationIndex = getIdentificationIndexABCD(unit);
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
