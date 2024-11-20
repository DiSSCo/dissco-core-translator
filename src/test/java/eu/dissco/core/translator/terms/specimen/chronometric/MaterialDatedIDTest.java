package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaterialDatedIDTest {

  private static final String MATERIAL_DATED_ID_STRING = "dwc:materialSampleID: https://www.ebi.ac.uk/metagenomics/samples/SRS1930158";
  private final MaterialDatedID materialDatedID = new MaterialDatedID();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:materialDatedID", MATERIAL_DATED_ID_STRING);

    // When
    var result = materialDatedID.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MATERIAL_DATED_ID_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = materialDatedID.getTerm();

    // Then
    assertThat(result).isEqualTo(MaterialDatedID.TERM);
  }
}
