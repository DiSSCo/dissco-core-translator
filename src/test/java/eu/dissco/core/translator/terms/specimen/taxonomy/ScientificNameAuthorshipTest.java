package eu.dissco.core.translator.terms.specimen.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScientificNameAuthorshipTest {
  private final ScientificNameAuthorship authorship = new ScientificNameAuthorship();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:scientificNameAuthorship", "G. Fischer");

    // When
    var result = authorship.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("G. Fischer");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank", "classis");
    unit.put("abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName", "Eurotatoria");
    unit.put("abcd:identifications/identification/0/result/taxonIdentified/nameAtomised/botanical/authorTeam", "Cham. &amp; Schltdl.");
    unit.put("abcd:identifications/identification/0/preferredFlag", true);

    // When
    var result = authorship.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("Cham. &amp; Schltdl.");
  }

  @Test
  void testGetTerm() {
    // When
    var result = authorship.getTerm();

    // Then
    assertThat(result).isEqualTo(ScientificNameAuthorship.TERM);
  }

}
