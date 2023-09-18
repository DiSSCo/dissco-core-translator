package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ModifiedTest {

  private final Modified modified = new Modified();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var modifiedString = "18-09-2023";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:modified", modifiedString);

    // When
    var result = modified.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(modifiedString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = modified.getTerm();

    // Then
    assertThat(result).isEqualTo(Modified.TERM);
  }

}
