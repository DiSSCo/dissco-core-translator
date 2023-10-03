package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NomenclaturalCodeTest {

  private final NomenclaturalCode nomenclaturalCode = new NomenclaturalCode();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var nomenclaturalCodeString = "A nomenclatural code";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:nomenclaturalCode", nomenclaturalCodeString);

    // When
    var result = nomenclaturalCode.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(nomenclaturalCodeString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var nomenclaturalCodeString = "A nomenclatural code";
    var unit = MAPPER.createObjectNode();
    unit.put("result/taxonIdentified/code", nomenclaturalCodeString);

    // When
    var result = nomenclaturalCode.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(nomenclaturalCodeString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = nomenclaturalCode.getTerm();

    // Then
    assertThat(result).isEqualTo(NomenclaturalCode.TERM);
  }
}
