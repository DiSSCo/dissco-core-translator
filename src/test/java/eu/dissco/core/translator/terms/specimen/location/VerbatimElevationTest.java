package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerbatimElevationTest {

  private static final String ELEVATION_STRING = "100-200 m";

  private final VerbatimElevation verbatimElevation = new VerbatimElevation();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verbatimElevation", ELEVATION_STRING);

    // When
    var result = verbatimElevation.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(ELEVATION_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = verbatimElevation.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimElevation.TERM);
  }
}
