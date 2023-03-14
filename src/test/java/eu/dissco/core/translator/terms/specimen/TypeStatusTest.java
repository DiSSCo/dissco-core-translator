package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    var unit = new ObjectMapper().createObjectNode();
    unit.put("dwc:typeStatus", STATUS);

    // When
    var result = typeStatus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(STATUS);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
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
    var unit = new ObjectMapper().createObjectNode();

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
