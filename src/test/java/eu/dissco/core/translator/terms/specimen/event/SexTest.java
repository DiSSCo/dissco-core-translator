package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SexTest {

  private static final String SEX_STRING = "Female";

  private final Sex sex = new Sex();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:sex", SEX_STRING);

    // When
    var result = sex.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(SEX_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:sex", SEX_STRING);

    // When
    var result = sex.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(SEX_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = sex.getTerm();

    // Then
    assertThat(result).isEqualTo(Sex.TERM);
  }

}
