package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DayTest {

  private static final String DAY_STRING = "12";

  private final Day day = new Day();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:day", DAY_STRING);

    // When
    var result = day.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(DAY_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = day.getTerm();

    // Then
    assertThat(result).isEqualTo(Day.TERM);
  }

}
