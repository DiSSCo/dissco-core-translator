package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InfraSpecificEpithetTest {

  private final InfraspecificEpithet infraSpecificEpithet = new InfraspecificEpithet();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:infraspecificEpithet", "thyrsoidea");

    // When
    var result = infraSpecificEpithet.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("thyrsoidea");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/scientificName/nameAtomised/botanical/infraspecificEpithet",
        "thyrsoidea");

    // When
    var result = infraSpecificEpithet.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("thyrsoidea");
  }

  @Test
  void testGetTerm() {
    // When
    var result = infraSpecificEpithet.getTerm();

    // Then
    assertThat(result).isEqualTo(InfraspecificEpithet.TERM);
  }
}
