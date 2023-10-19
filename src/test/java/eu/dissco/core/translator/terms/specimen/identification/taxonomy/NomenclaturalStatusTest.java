package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NomenclaturalStatusTest {

  private final NomenclaturalStatus nomenclaturalStatus = new NomenclaturalStatus();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var acceptedNameUsageString = "nom. illeg.";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:nomenclaturalStatus", acceptedNameUsageString);

    // When
    var result = nomenclaturalStatus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(acceptedNameUsageString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = nomenclaturalStatus.getTerm();

    // Then
    assertThat(result).isEqualTo(NomenclaturalStatus.TERM);
  }
}
