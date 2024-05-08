package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerbatimLocalityTest {

  private static final String VERBATIM_LOCALITY_STRING = "25 km NNE Bariloche por R. Nac. 237";

  private final VerbatimLocality verbatimLocality = new VerbatimLocality();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verbatimLocality", VERBATIM_LOCALITY_STRING);

    // When
    var result = verbatimLocality.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM_LOCALITY_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = verbatimLocality.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimLocality.TERM);
  }
}
