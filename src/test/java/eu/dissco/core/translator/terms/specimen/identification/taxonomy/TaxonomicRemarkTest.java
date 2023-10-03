package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaxonomicRemarkTest {

  private final TaxonRemarks taxonRemarks = new TaxonRemarks();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var taxonRemarksString = "An interesting remark about a taxon";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:taxonRemarks", taxonRemarksString);

    // When
    var result = taxonRemarks.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(taxonRemarksString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = taxonRemarks.getTerm();

    // Then
    assertThat(result).isEqualTo(TaxonRemarks.TERM);
  }
}
