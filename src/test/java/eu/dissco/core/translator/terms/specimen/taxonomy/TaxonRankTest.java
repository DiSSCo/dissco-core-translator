package eu.dissco.core.translator.terms.specimen.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaxonRankTest {

  private final TaxonRank taxonRank = new TaxonRank();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:taxonRank", "Species");

    // When
    var result = taxonRank.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Species");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();

    // When
    var result = taxonRank.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = taxonRank.getTerm();

    // Then
    assertThat(result).isEqualTo(TaxonRank.TERM);
  }
}
