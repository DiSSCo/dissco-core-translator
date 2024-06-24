package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

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
  void testRetrieveFromDWCAIsEmpty() {
    // Given
    var organismQuantityTypeString = "Some nice quantitative type";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:organismQuantityType", organismQuantityTypeString);

    // When
    var result = organismQuantityType.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(organismQuantityTypeString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = organismQuantityType.getTerm();

    // Then
    assertThat(result).isEqualTo(OrganismQuantityType.TERM);
  }

}
