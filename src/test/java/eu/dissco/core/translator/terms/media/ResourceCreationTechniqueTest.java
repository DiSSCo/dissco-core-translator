package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResourceCreationTechniqueTest {

  private final ResourceCreationTechnique resourceCreationTechnique = new ResourceCreationTechnique();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var resourceCreationTechniqueString = "Multiflash lighting";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:resourceCreationTechnique", resourceCreationTechniqueString);

    // When
    var result = this.resourceCreationTechnique.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(resourceCreationTechniqueString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = resourceCreationTechnique.getTerm();

    // Then
    assertThat(result).isEqualTo(ResourceCreationTechnique.TERM);
  }

}
