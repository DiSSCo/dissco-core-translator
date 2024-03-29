package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CultivarEpithetTest {

  private final CultivarEpithet cultivarEpithet = new CultivarEpithet();

  private final String cultivarEpithetString = "King Edward";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:cultivarEpithet", cultivarEpithetString);

    // When
    var result = cultivarEpithet.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(cultivarEpithetString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/scientificName/nameAtomised/botanical/cultivarName",
        cultivarEpithetString);

    // When
    var result = cultivarEpithet.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(cultivarEpithetString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = cultivarEpithet.getTerm();

    // Then
    assertThat(result).isEqualTo(CultivarEpithet.TERM);
  }
}
