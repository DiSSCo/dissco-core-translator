package eu.dissco.core.translator.terms.specimen.occurence;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganismQuantityTest {

  private static final String ORGANISM_QUANTITY_STRING = "3";

  private final OrganismQuantity organismQuantity = new OrganismQuantity();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:individualCount", ORGANISM_QUANTITY_STRING);

    // When
    var result = organismQuantity.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(ORGANISM_QUANTITY_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = organismQuantity.getTerm();

    // Then
    assertThat(result).isEqualTo(OrganismQuantity.TERM);
  }

}
