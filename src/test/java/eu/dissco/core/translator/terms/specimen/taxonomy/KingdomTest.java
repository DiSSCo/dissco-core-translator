package eu.dissco.core.translator.terms.specimen.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KingdomTest {
  private final Kingdom kingdom = new Kingdom();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:kingdom", "Animalia");

    // When
    var result = kingdom.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Animalia");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank", "regnum");
    unit.put("abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName", "Plantae");

    // When
    var result = kingdom.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("Plantae");
  }

  @Test
  void testGetTerm() {
    // When
    var result = kingdom.getTerm();

    // Then
    assertThat(result).isEqualTo(Kingdom.TERM);
  }

}
