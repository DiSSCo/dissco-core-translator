package eu.dissco.core.translator.terms.specimen.occurence;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DegreeOfEstablishmentTest {

  private static final String DEGREE_OF_ESTABLISHMENT_STRING = "http://rs.tdwg.org/dwcdoe/values/d003";

  private final DegreeOfEstablishment degreeOfEstablishment = new DegreeOfEstablishment();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:degreeOfEstablishment", DEGREE_OF_ESTABLISHMENT_STRING);

    // When
    var result = degreeOfEstablishment.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(DEGREE_OF_ESTABLISHMENT_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = degreeOfEstablishment.getTerm();

    // Then
    assertThat(result).isEqualTo(DegreeOfEstablishment.TERM);
  }

}
