package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MaterialDatedRelationshipTest {

  private static final String MATERIAL_RELATIONSHIP = "stratigraphicallyCorrelatedWith";
  private final MaterialDatedRelationship materialDatedRelationship = new MaterialDatedRelationship();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:materialDatedRelationship", MATERIAL_RELATIONSHIP);

    // When
    var result = materialDatedRelationship.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MATERIAL_RELATIONSHIP);
  }

  @Test
  void testGetTerm() {
    // When
    var result = materialDatedRelationship.getTerm();

    // Then
    assertThat(result).isEqualTo(MaterialDatedRelationship.TERM);
  }
}
