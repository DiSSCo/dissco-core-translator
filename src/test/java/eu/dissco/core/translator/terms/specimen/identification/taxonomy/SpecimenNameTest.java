package eu.dissco.core.translator.terms.specimen.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.terms.specimen.identification.taxonomy.SpecimenName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpecimenNameTest {

  private final SpecimenName specimenName = new SpecimenName();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var specimenNameString = "SpecimenName";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:scientificName", specimenNameString);

    // When
    var result = specimenName.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(specimenNameString);
  }

  @Test
  void testRetrieveFromDWCAExtension() {
    // Given
    var identificationVerified = MAPPER.createObjectNode();
    identificationVerified.put("dwc:scientificName", "SpecimenNameVerified");
    identificationVerified.put("dwc:identificationVerificationStatus", "1");

    // When
    var result = specimenName.retrieveFromDWCA(identificationVerified);

    // Then
    assertThat(result).isEqualTo("SpecimenNameVerified");
  }

  @Test
  void testRetrieveFromABCDEFG() {
    // Given
    var specimenNameString = "SpecimenName";
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/mineralRockIdentified/classifiedName/fullScientificNameString",
        specimenNameString);

    // When
    var result = specimenName.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(specimenNameString);
  }

  @Test
  void testRetrieveFromABCDWithPreferred() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/scientificName/fullScientificNameString",
        "Bignonia chica Humb. & Bonpl.");

    // When
    var result = specimenName.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("Bignonia chica Humb. & Bonpl.");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var specimenNameString = "SpecimenName";
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/scientificName/fullScientificNameString",
        specimenNameString);

    // When
    var result = specimenName.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(specimenNameString);
  }

  @Test
  void testRetrieveFromABCDNotPresent() {
    // Given
    var unit = MAPPER.createObjectNode();

    // When
    var result = specimenName.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }


  @Test
  void testGetTerm() {
    // When
    var result = specimenName.getTerm();

    // Then
    assertThat(result).isEqualTo(SpecimenName.TERM);
  }

}
