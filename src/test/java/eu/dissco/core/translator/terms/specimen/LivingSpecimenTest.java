package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LivingSpecimenTest {

  private static final String LIVING_SPECIMEN = "Living";
  private final LivingSpecimen livingSpecimen = new LivingSpecimen();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:basisOfRecord", "LivingSpecimen");

    // When
    var result = livingSpecimen.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(LIVING_SPECIMEN);
  }

  @Test
  void testRetrieveFromDWCANotLiving() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:basisOfRecord", "PreservedSpecimen");

    // When
    var result = livingSpecimen.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Preserved");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:recordBasis", "LivingSpecimen");

    // When
    var result = livingSpecimen.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(LIVING_SPECIMEN);
  }

  @Test
  void testGetTerm() {
    // When
    var result = livingSpecimen.getTerm();

    // Then
    assertThat(result).isEqualTo(LivingSpecimen.TERM);
  }

}
