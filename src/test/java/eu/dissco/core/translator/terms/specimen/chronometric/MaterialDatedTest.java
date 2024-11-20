package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaterialDatedTest {

  private static final String MATERIAL_DATED_STRING = "charred wood";
  private final MaterialDated materialDated = new MaterialDated();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:materialDated", MATERIAL_DATED_STRING);

    // When
    var result = materialDated.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MATERIAL_DATED_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("materialDated", MATERIAL_DATED_STRING);

    // When
    var result = materialDated.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MATERIAL_DATED_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = materialDated.getTerm();

    // Then
    assertThat(result).isEqualTo(MaterialDated.TERM);
  }
}
