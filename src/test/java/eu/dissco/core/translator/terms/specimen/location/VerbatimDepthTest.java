package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerbatimDepthTest {

  private static final String DEPTH_STRING = "100-200 m";

  private final VerbatimDepth verbatimDepth = new VerbatimDepth();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verbatimDepth", DEPTH_STRING);

    // When
    var result = verbatimDepth.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(DEPTH_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = verbatimDepth.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimDepth.TERM);
  }
}
