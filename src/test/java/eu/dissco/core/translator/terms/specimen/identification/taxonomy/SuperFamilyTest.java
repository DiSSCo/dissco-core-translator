package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SuperFamilyTest {

  private final Superfamily superfamily = new Superfamily();
  private final String superfamilyString = "Cerithioidea";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:superfamily", superfamilyString);

    // When
    var result = superfamily.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(superfamilyString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank",
        "superfamilia");
    unit.put(
        "result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName",
        superfamilyString);

    // When
    var result = superfamily.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(superfamilyString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = superfamily.getTerm();

    // Then
    assertThat(result).isEqualTo(Superfamily.TERM);
  }
}
