package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BasisOfRecordTest {

  private final BasisOfRecord basisOfRecord = new BasisOfRecord();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:basisOfRecord", "LivingSpecimen");

    // When
    var result = basisOfRecord.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("LivingSpecimen");
  }

  @Test
  void testRetrieveFromDWCANotLiving() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:basisOfRecord", "PreservedSpecimen");

    // When
    var result = basisOfRecord.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("PreservedSpecimen");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:recordBasis", "RockSpecimen");

    // When
    var result = basisOfRecord.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("RockSpecimen");
  }

  @Test
  void testGetTerm() {
    // When
    var result = basisOfRecord.getTerm();

    // Then
    assertThat(result).isEqualTo(BasisOfRecord.TERM);
  }
}
