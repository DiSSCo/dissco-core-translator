package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InfraGenericEpithetTest {

  private final InfraGenericEpithet infraGenericEpithet = new InfraGenericEpithet();

  private final String infraGenericEpithetString = "Abacetillus";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:infragenericEpithet", infraGenericEpithetString);

    // When
    var result = infraGenericEpithet.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(infraGenericEpithetString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/scientificName/nameAtomised/zoological/subgenus",
        infraGenericEpithetString);

    // When
    var result = infraGenericEpithet.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(infraGenericEpithetString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = infraGenericEpithet.getTerm();

    // Then
    assertThat(result).isEqualTo(InfraGenericEpithet.TERM);
  }
}
