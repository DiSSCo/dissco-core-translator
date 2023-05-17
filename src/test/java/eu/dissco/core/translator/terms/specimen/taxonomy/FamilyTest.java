package eu.dissco.core.translator.terms.specimen.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FamilyTest {

  private final Family family = new Family();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:family", "Soricidae");

    // When
    var result = family.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Soricidae");
  }

  @Test
  void testRetrieveFromDWCAExtensionNoVerified() {
    // Given
    var unit = MAPPER.createObjectNode();
    var extension = MAPPER.createObjectNode();
    var identification = MAPPER.createObjectNode();
    identification.put("dwc:family", "Soricidae");
    var identifications = MAPPER.createArrayNode();
    identifications.add(identification);
    extension.set("dwc:Identification", identifications);
    unit.set("extensions", extension);

    // When
    var result = family.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Soricidae");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank",
        "familia");
    unit.put(
        "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName",
        "BIGNONIACEAE");

    // When
    var result = family.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("BIGNONIACEAE");
  }

  @Test
  void testGetTerm() {
    // When
    var result = family.getTerm();

    // Then
    assertThat(result).isEqualTo(Family.TERM);
  }

}
