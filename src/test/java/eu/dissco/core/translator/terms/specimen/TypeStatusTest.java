package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TypeStatusTest {

  private static final String STATUS = "holotype | FULL_NAME | CITATION";

  private final TypeStatus typeStatus = new TypeStatus();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:typeStatus", STATUS);

    // When
    var result = typeStatus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(STATUS);
  }

  @Test
  void testRetrieveFromDWCAInExtension() {
    // Given
    var unit = MAPPER.createObjectNode();
    var array = MAPPER.createArrayNode();
    var extension = MAPPER.createObjectNode();
    extension.put("dwc:typeStatus", STATUS);
    array.add(extension);
    var extensions = MAPPER.createObjectNode();
    extensions.set("dwc:Identification", array);
    unit.set("extensions", extensions);

    // When
    var result = typeStatus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(STATUS);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typeStatus",
        "holotype");
    unit.put(
        "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typifiedName/fullScientificNameString",
        "FULL_NAME");
    unit.put(
        "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/nomenclaturalReference/titleCitation",
        "CITATION");
    unit.put(
        "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/1/typeStatus",
        "Another_Status");

    // When
    var result = typeStatus.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(STATUS);
  }

  @Test
  void testRetrieveFromABCDNoTypeStatus() {
    // Given
    var unit = MAPPER.createObjectNode();

    // When
    var result = typeStatus.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = typeStatus.getTerm();

    // Then
    assertThat(result).isEqualTo(TypeStatus.TERM);
  }

}
