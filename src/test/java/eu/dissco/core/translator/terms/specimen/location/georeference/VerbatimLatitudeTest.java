package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VerbatimLatitudeTest {

  private static final String VERBATIM_LATITUDE = "41 05 54.03S";

  private final VerbatimLatitude verbatimLatitude = new VerbatimLatitude();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verbatimLatitude", VERBATIM_LATITUDE);

    // When
    var result = verbatimLatitude.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM_LATITUDE);
  }

  @Test
  void testGetTerm() {
    // When
    var result = verbatimLatitude.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimLatitude.TERM);
  }
}
