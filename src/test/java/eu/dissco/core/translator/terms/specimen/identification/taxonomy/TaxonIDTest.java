package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaxonIDTest {

  private final TaxonID taxonId = new TaxonID();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var taxonIdString = "UDS:283845";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:taxonID", taxonIdString);

    // When
    var result = taxonId.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(taxonIdString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = taxonId.getTerm();

    // Then
    assertThat(result).isEqualTo(TaxonID.TERM);
  }
}
