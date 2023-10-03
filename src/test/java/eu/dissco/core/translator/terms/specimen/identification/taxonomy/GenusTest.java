package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GenusTest {

  private final Genus genus = new Genus();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:genus", "Rhinopoma");

    // When
    var result = genus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Rhinopoma");
  }

  @Test
  void testRetrieveFromDWCAExtensionNoVerified() {
    // Given
    var identificationVerified = MAPPER.createObjectNode();
    identificationVerified.put("dwc:genus", "Rhinopoma");

    // When
    var result = genus.retrieveFromDWCA(identificationVerified);

    // Then
    assertThat(result).isEqualTo("Rhinopoma");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/scientificName/nameAtomised/botanical/genusOrMonomial",
        "Arrabidaea");

    // When
    var result = genus.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("Arrabidaea");
  }

  @Test
  void testGetTerm() {
    // When
    var result = genus.getTerm();

    // Then
    assertThat(result).isEqualTo(Genus.TERM);
  }
}
