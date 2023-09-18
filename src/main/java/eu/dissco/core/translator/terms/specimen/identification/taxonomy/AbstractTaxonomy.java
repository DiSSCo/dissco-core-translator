package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.Term;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public abstract class AbstractTaxonomy extends Term {

  private static final Pair<String, String> ABCD_TAXON_RANK =
      Pair.of("result/taxonIdentified/higherTaxa/higherTaxon/",
          "/higherTaxonRank");
  private static final Pair<String, String> ABCD_VALUE =
      Pair.of("result/taxonIdentified/higherTaxa/higherTaxon/",
          "/higherTaxonName");

  protected String searchABCDSplitTerms(JsonNode unit, List<String> searchTerms) {
    return searchABCDSplitTerms(unit, searchTerms,
        Pair.of(ABCD_TAXON_RANK.getLeft(), ABCD_TAXON_RANK.getRight()),
        Pair.of(ABCD_VALUE.getLeft(), ABCD_VALUE.getRight()));
  }

}
