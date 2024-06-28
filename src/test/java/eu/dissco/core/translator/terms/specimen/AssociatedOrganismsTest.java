package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssociatedOrganismsTest {

  private static final String ASSOCIATED_ORGANISMS_STRING = "sibling of http://arctos.database.museum/guid/DMNS:Mamm:14171";
  private final AssociatedOrganisms associatedOrganisms = new AssociatedOrganisms();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:associatedOrganisms", ASSOCIATED_ORGANISMS_STRING);

    // When
    var result = associatedOrganisms.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(ASSOCIATED_ORGANISMS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = associatedOrganisms.getTerm();

    // Then
    assertThat(result).isEqualTo(AssociatedOrganisms.TERM);
  }
}
