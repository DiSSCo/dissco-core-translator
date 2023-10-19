package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdentificationQualifierTest {

  private final IdentificationQualifier identificationQualifier = new IdentificationQualifier();
  private final String IdentificationQualifierString = "cf. var. oxyadenia";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:identificationQualifier", IdentificationQualifierString);

    // When
    var result = identificationQualifier.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(IdentificationQualifierString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("result/taxonIdentified/scientificName/identificationQualifier/nameAddendum",
        IdentificationQualifierString);

    // When
    var result = identificationQualifier.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(IdentificationQualifierString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = identificationQualifier.getTerm();

    // Then
    assertThat(result).isEqualTo(IdentificationQualifier.TERM);
  }

}
