package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganismNameTest {

  private static final String ORGANISM_NAME_STRING = "Boab Prison Tree";
  private final OrganismName organismName = new OrganismName();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:organismName", ORGANISM_NAME_STRING);

    // When
    var result = organismName.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(ORGANISM_NAME_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = organismName.getTerm();

    // Then
    assertThat(result).isEqualTo(OrganismName.TERM);
  }
}
