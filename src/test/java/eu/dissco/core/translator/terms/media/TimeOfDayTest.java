package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TimeOfDayTest {

  private final TimeOfDay timeOfDay = new TimeOfDay();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var timeOfDayString = "afternoon";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:timeOfDay", timeOfDayString);

    // When
    var result = this.timeOfDay.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(timeOfDayString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = timeOfDay.getTerm();

    // Then
    assertThat(result).isEqualTo(TimeOfDay.TERM);
  }

}
