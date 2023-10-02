package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScientificNameTest {

  private final ScientificName scientificname = new ScientificName();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var scientificNameString = "SpecimenName";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:scientificName", scientificNameString);

    // When
    var result = scientificname.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(scientificNameString);
  }

  @Test
  void testRetrieveFromDWCAExtension() {
    // Given
    var identificationVerified = MAPPER.createObjectNode();
    identificationVerified.put("dwc:scientificName", "SpecimenNameVerified");

    // When
    var result = scientificname.retrieveFromDWCA(identificationVerified);

    // Then
    assertThat(result).isEqualTo("SpecimenNameVerified");
  }

  @Test
  void testRetrieveFromABCDEFG() {
    // Given
    var scientificNameString = "SpecimenName";
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/mineralRockIdentified/classifiedName/fullScientificNameString",
        scientificNameString);

    // When
    var result = scientificname.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(scientificNameString);
  }

  @Test
  void testRetrieveFromABCDWithPreferred() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/scientificName/fullScientificNameString",
        "Bignonia chica Humb. & Bonpl.");
    unit.put(
        "result/mineralRockIdentified/classifiedName/fullScientificNameString",
        "Another specimen name");

    // When
    var result = scientificname.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("Bignonia chica Humb. & Bonpl.");
  }

  @Test
  void testRetrieveFromABCDNotPresent() {
    // Given
    var unit = MAPPER.createObjectNode();

    // When
    var result = scientificname.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }


  @Test
  void testGetTerm() {
    // When
    var result = scientificname.getTerm();

    // Then
    assertThat(result).isEqualTo(ScientificName.TERM);
  }

}
