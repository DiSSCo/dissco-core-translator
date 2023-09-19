package eu.dissco.core.translator.terms.specimen.occurence;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.impl.QOM.Or;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganismQuantityTypeTest {

  private final OrganismQuantityType organismQuantityType = new OrganismQuantityType();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:individualCount", "5");

    // When
    var result = organismQuantityType.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("individuals");
  }
  @Test
  void testGetTerm() {
    // When
    var result = organismQuantityType.getTerm();

    // Then
    assertThat(result).isEqualTo(OrganismQuantityType.TERM);
  }

}
