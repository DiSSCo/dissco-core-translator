package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubTribeTest {

  private final SubTribe subTribe = new SubTribe();
  private final String subTribeString = "Plotinini";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:subtribe", subTribeString);

    // When
    var result = subTribe.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(subTribeString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank",
        "subtribus");
    unit.put(
        "result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName",
        subTribeString);

    // When
    var result = subTribe.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(subTribeString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = subTribe.getTerm();

    // Then
    assertThat(result).isEqualTo(SubTribe.TERM);
  }
}
