package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.terms.specimen.identification.taxonomy.SpecificEpithet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpecificEpithetTest {

  private final SpecificEpithet specificEpithet = new SpecificEpithet();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:specificEpithet", "gottschei");

    // When
    var result = specificEpithet.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("gottschei");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/scientificName/nameAtomised/botanical/firstEpithet", "bogotensis");

    // When
    var result = specificEpithet.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("bogotensis");
  }

  @Test
  void testGetTerm() {
    // When
    var result = specificEpithet.getTerm();

    // Then
    assertThat(result).isEqualTo(SpecificEpithet.TERM);
  }
}
