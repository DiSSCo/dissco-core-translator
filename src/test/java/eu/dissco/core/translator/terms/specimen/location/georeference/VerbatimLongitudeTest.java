package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VerbatimLongitudeTest {

  private static final String VERBATIM_LONGITUDE = "121d 10' 34\" W";

  private final VerbatimLongitude verbatimLongitude = new VerbatimLongitude();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verbatimLongitude", VERBATIM_LONGITUDE);

    // When
    var result = verbatimLongitude.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM_LONGITUDE);
  }

  @Test
  void testGetTerm() {
    // When
    var result = verbatimLongitude.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimLongitude.TERM);
  }
}
