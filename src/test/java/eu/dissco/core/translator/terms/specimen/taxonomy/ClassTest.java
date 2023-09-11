package eu.dissco.core.translator.terms.specimen.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Class;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClassTest {

  private final Class classTax = new Class();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:class", "Mammalia");

    // When
    var result = classTax.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Mammalia");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank",
        "classis");
    unit.put(
        "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName",
        "Eurotatoria");
    unit.put("abcd:identifications/identification/0/preferredFlag", false);
    unit.put(
        "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank",
        "classis");
    unit.put(
        "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName",
        "Mammalia");
    unit.put(
        "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/1/higherTaxonRank",
        "regnum");
    unit.put(
        "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/1/higherTaxonName",
        "Animalia");
    unit.put("abcd:identifications/identification/1/preferredFlag", true);

    // When
    var result = classTax.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("Mammalia");
  }

  @Test
  void testGetTerm() {
    // When
    var result = classTax.getTerm();

    // Then
    assertThat(result).isEqualTo(Class.TERM);
  }
}
