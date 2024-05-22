package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerbatimSRSTest {

  private static final String VERBATIM_SRS = "NAD27";

  private final VerbatimSRS verbatimSRS = new VerbatimSRS();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verbatimSRS", VERBATIM_SRS);

    // When
    var result = verbatimSRS.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM_SRS);
  }

  @Test
  void testGetTerm() {
    // When
    var result = verbatimSRS.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimSRS.TERM);
  }
}
