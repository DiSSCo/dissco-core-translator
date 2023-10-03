package eu.dissco.core.translator.terms.specimen.occurence;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MOCK_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventDateTest {

  private final EventDate eventData = new EventDate();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:eventDate", MOCK_DATE);

    // When
    var result = eventData.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MOCK_DATE);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/dateTime/isodateTimeBegin", MOCK_DATE);

    // When
    var result = eventData.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MOCK_DATE);
  }

  @Test
  void testGetTerm() {
    // When
    var result = eventData.getTerm();

    // Then
    assertThat(result).isEqualTo(EventDate.TERM);
  }

}
