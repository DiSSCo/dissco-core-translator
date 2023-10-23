package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubtribeTest {

  private final Subtribe subtribe = new Subtribe();
  private final String subtribeString = "Plotinini";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:subtribe", subtribeString);

    // When
    var result = subtribe.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(subtribeString);
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
        subtribeString);

    // When
    var result = subtribe.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(subtribeString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = subtribe.getTerm();

    // Then
    assertThat(result).isEqualTo(Subtribe.TERM);
  }
}
