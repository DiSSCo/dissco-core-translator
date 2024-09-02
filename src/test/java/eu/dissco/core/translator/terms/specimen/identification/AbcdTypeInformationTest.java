package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.Identification;
import eu.dissco.core.translator.schema.OdsHasTaxonIdentification;
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
    assertThat(digitalSpecimen.getOdsHasIdentification().get(0))
        .extracting(Identification::getDwcTypeStatus)
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
    assertThat(digitalSpecimen.getOdsHasIdentification().get(0))
        .extracting(Identification::getDwcTypeStatus)
        .isEqualTo("isotype");
    assertThat(digitalSpecimen.getOdsHasIdentification().get(1))
        .extracting(Identification::getDwcTypeStatus)
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
    assertThat(digitalSpecimen.getOdsHasIdentification().get(0))
        .extracting(Identification::getDwcTypeStatus)
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
            name -> new Identification().withOdsHasTaxonIdentification(
                List.of(new OdsHasTaxonIdentification().withDwcScientificName(name))))
        .toList();
    return new DigitalSpecimen()
        .withOdsHasIdentification(identifications);
  }

}
