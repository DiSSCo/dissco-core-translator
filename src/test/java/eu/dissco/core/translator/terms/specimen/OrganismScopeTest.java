package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganismScopeTest {

  private static final String ORGANISM_SCOPE_STRING = "colony";
  private final OrganismScope organismScope = new OrganismScope();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:organismScope", ORGANISM_SCOPE_STRING);

    // When
    var result = organismScope.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(ORGANISM_SCOPE_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = organismScope.getTerm();

    // Then
    assertThat(result).isEqualTo(OrganismScope.TERM);
  }
}
