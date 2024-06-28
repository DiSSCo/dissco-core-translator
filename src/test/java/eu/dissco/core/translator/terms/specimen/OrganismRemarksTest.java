package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganismRemarksTest {

  private static final String ORGANISM_REMARKS_STRING = "One of a litter of six";
  private final OrganismRemarks organismRemarks = new OrganismRemarks();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:organismRemarks", ORGANISM_REMARKS_STRING);

    // When
    var result = organismRemarks.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(ORGANISM_REMARKS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = organismRemarks.getTerm();

    // Then
    assertThat(result).isEqualTo(OrganismRemarks.TERM);
  }
}
