package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RightsHolderTest {

  private final RightsHolder rightsHolder = new RightsHolder();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var rightsHolderString = "The rightsHolder";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:rightsHolder", rightsHolderString);

    // When
    var result = rightsHolder.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(rightsHolderString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = rightsHolder.getTerm();

    // Then
    assertThat(result).isEqualTo(RightsHolder.TERM);
  }

}
