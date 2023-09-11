package eu.dissco.core.translator.terms.specimen.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.terms.specimen.identification.taxonomy.Genus;
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
    var unit = MAPPER.createObjectNode();
    var extension = MAPPER.createObjectNode();
    var identificationUnverified = MAPPER.createObjectNode();
    identificationUnverified.put("dwc:genus", "Rhinopoma");
    identificationUnverified.put("dwc:identificationVerificationStatus", "0");
    var identificationVerified = MAPPER.createObjectNode();
    identificationVerified.put("dwc:genus", "Rhinopoma");
    identificationVerified.put("dwc:identificationVerificationStatus", "1");
    var identifications = MAPPER.createArrayNode();
    identifications.add(identificationUnverified);
    identifications.add(identificationVerified);
    extension.set("dwc:Identification", identifications);
    unit.set("extensions", extension);
    unit.put("ods:taxonIdentificationIndex", "1");

    // When
    var result = genus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Rhinopoma");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank",
        "classis");
    unit.put(
        "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName",
        "Eurotatoria");
    unit.put(
        "abcd:identifications/identification/0/result/taxonIdentified/nameAtomised/botanical/genusOrMonomial",
        "Fridericia Mart.");
    unit.put("abcd:identifications/identification/0/preferredFlag", false);
    unit.put(
        "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank",
        "classis");
    unit.put(
        "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName",
        "Mammalia");
    unit.put(
        "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/1/higherTaxonRank",
        "regnum");
    unit.put(
        "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/1/higherTaxonName",
        "Animalia");
    unit.put(
        "abcd:identifications/identification/1/result/taxonIdentified/nameAtomised/botanical/genusOrMonomial",
        "Arrabidaea");

    unit.put("abcd:identifications/identification/1/preferredFlag", true);

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
