package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.terms.specimen.identification.taxonomy.TaxonomicStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TypeStatusTest {

  private final TypeStatus typeStatus = new TypeStatus();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var typeStatusString = "holotype";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:typeStatus", typeStatusString);

    // When
    var result = typeStatus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(typeStatusString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = typeStatus.getTerm();

    // Then
    assertThat(result).isEqualTo(TypeStatus.TERM);
  }

}
