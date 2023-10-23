package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TribeTest {

  private final Tribe tribe = new Tribe();
  private final String tribeString = "Arethuseae";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:tribe", tribeString);

    // When
    var result = tribe.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(tribeString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank",
        "tribus");
    unit.put(
        "result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName",
        tribeString);

    // When
    var result = tribe.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(tribeString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = tribe.getTerm();

    // Then
    assertThat(result).isEqualTo(Tribe.TERM);
  }
}
