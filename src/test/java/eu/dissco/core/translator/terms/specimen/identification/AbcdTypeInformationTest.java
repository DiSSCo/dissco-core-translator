package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.Identifications;
import eu.dissco.core.translator.schema.TaxonIdentification;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AbcdTypeInformationTest {

  private AbcdTypeInformation abcdTypeInformation;

  @BeforeEach
  void setup() {
    abcdTypeInformation = new AbcdTypeInformation();
  }

  @Test
  void addTypeInformationSingleIdentification() {
    // Given
    var json = givenJson(false);
    var digitalSpecimen = givenDigitalSpecimenWithIdentification(
        List.of("Minthostachys mollis (Kunth) Griseb."));

    // When
    abcdTypeInformation.addTypeInformation(digitalSpecimen, json, MAPPER);

    // Then
    assertThat(digitalSpecimen.getDwcIdentification().get(0))
        .extracting(Identifications::getDwcTypeStatus)
        .isEqualTo("isotype");
  }

  @Test
  void addTypeInformationMultipleIdentifications() {
    // Given
    var json = givenJson(true);
    var digitalSpecimen = givenDigitalSpecimenWithIdentification(
        List.of("Minthostachys mollis (Kunth) Griseb.", "Bystropogon reticulatus Willd. ex Steud.",
            "Bystropogon reticulatus"));

    // When
    abcdTypeInformation.addTypeInformation(digitalSpecimen, json, MAPPER);

    // Then
    assertThat(digitalSpecimen.getDwcIdentification().get(0))
        .extracting(Identifications::getDwcTypeStatus)
        .isEqualTo("isotype");
    assertThat(digitalSpecimen.getDwcIdentification().get(1))
        .extracting(Identifications::getDwcTypeStatus)
        .isNull();
  }

  @Test
  void addTypeInformationNoMatch() {
    // Given
    var json = givenJson(false);
    var digitalSpecimen = givenDigitalSpecimenWithIdentification(
        List.of("Minthostachys mollis (Kunth) Griseb.", "Bystropogon reticulatus Willd. ex Steud.",
            "Bystropogon reticulatus"));

    // When
    abcdTypeInformation.addTypeInformation(digitalSpecimen, json, MAPPER);

    // Then
    assertThat(digitalSpecimen.getDwcIdentification().get(0))
        .extracting(Identifications::getDwcTypeStatus)
        .isNull();
  }

  private JsonNode givenJson(boolean withTypifiedName) {
    var objectNode = MAPPER.createObjectNode();
    objectNode.put(
        "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typeStatus",
        "isotype");
    if (withTypifiedName) {
      objectNode.put(
          "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typifiedName/fullScientificNameString",
          "Minthostachys mollis (Kunth) Griseb.");
    }
    return objectNode;
  }


  private DigitalSpecimen givenDigitalSpecimenWithIdentification(List<String> names) {
    var identifications = names.stream().map(
            name -> new Identifications().withTaxonIdentifications(
                List.of(new TaxonIdentification().withDwcScientificName(name))))
        .toList();
    return new DigitalSpecimen()
        .withDwcIdentification(identifications);
  }

}
