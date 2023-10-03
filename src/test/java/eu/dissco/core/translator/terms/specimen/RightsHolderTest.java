package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RightsHolderTest {

  private static final String RIGHTS_HOLDER_STRING = "S. Leeflang";
  private final RightsHolder rightsHolder = new RightsHolder();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:rightsHolder", RIGHTS_HOLDER_STRING);

    // When
    var result = rightsHolder.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(RIGHTS_HOLDER_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = rightsHolder.getTerm();

    // Then
    assertThat(result).isEqualTo(RightsHolder.TERM);
  }
}
