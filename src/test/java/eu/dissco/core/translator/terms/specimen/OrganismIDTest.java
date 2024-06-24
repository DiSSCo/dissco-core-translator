package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganismIDTest {

  private static final String ORGANISM_ID_STRING = "http://arctos.database.museum/guid/WNMU:Mamm:1249";
  private final OrganismID organismID = new OrganismID();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:organismID", ORGANISM_ID_STRING);

    // When
    var result = organismID.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(ORGANISM_ID_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = organismID.getTerm();

    // Then
    assertThat(result).isEqualTo(OrganismID.TERM);
  }
}
