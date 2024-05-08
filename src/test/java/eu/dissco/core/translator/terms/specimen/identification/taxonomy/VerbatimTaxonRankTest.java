package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerbatimTaxonRankTest {

  private final VerbatimTaxonRank verbatimTaxonRank = new VerbatimTaxonRank();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verbatimTaxonRank", "Agamospecies");

    // When
    var result = verbatimTaxonRank.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Agamospecies");
  }

  @Test
  void testGetTerm() {
    // When
    var result = verbatimTaxonRank.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimTaxonRank.TERM);
  }
}
