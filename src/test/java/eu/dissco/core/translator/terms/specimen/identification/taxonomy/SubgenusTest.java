package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubgenusTest {

  private final Subgenus subgenus = new Subgenus();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var subgenusString = "A new subgenus";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:subgenus", subgenusString);

    // When
    var result = subgenus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(subgenusString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var subgenusString = "A new subgenus";
    var unit = MAPPER.createObjectNode();
    unit.put("result/taxonIdentified/scientificName/nameAtomised/zoological/subgenus", subgenusString);

    // When
    var result = subgenus.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(subgenusString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = subgenus.getTerm();

    // Then
    assertThat(result).isEqualTo(Subgenus.TERM);
  }
}
