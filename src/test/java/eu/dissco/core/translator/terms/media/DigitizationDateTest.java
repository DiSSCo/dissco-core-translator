package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DigitizationDateTest {

  private final DigitizationDate digitizationDate = new DigitizationDate();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var digitizationDateString = "2023-10-01";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:digitizationDate", digitizationDateString);

    // When
    var result = this.digitizationDate.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(digitizationDateString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = digitizationDate.getTerm();

    // Then
    assertThat(result).isEqualTo(DigitizationDate.TERM);
  }

}
