package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PathwayTest {

  private static final String PATHWAY_STRING = "transportStowaway";

  private final Pathway pathway = new Pathway();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:pathway", PATHWAY_STRING);

    // When
    var result = pathway.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(PATHWAY_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = pathway.getTerm();

    // Then
    assertThat(result).isEqualTo(Pathway.TERM);
  }

}
