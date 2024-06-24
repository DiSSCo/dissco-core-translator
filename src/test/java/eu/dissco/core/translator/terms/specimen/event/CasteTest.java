package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CasteTest {

  private static final String CASTE_STRING = "queen";

  private final Caste caste = new Caste();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:caste", CASTE_STRING);

    // When
    var result = caste.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(CASTE_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = caste.getTerm();

    // Then
    assertThat(result).isEqualTo(Caste.TERM);
  }

}
