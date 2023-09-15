package eu.dissco.core.translator.terms.specimen.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Phylum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhylumTest {

  private final Phylum phylum = new Phylum();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:phylum", "Chordata");

    // When
    var result = phylum.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Chordata");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank",
        "phylum");
    unit.put(
        "result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName",
        "Chordata");

    // When
    var result = phylum.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("Chordata");
  }

  @Test
  void testGetTerm() {
    // When
    var result = phylum.getTerm();

    // Then
    assertThat(result).isEqualTo(Phylum.TERM);
  }
}
