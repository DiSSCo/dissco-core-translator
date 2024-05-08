package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScientificNameIdTest {

  private final ScientificNameId scientificnameId = new ScientificNameId();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var scientificNameString = "urn:lsid:ipni.org:names:37829-1:1.3";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:scientificNameID", scientificNameString);

    // When
    var result = scientificnameId.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(scientificNameString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = scientificnameId.getTerm();

    // Then
    assertThat(result).isEqualTo(ScientificNameId.TERM);
  }

}
