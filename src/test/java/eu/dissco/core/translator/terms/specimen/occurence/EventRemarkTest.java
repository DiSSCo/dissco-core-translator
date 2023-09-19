package eu.dissco.core.translator.terms.specimen.occurence;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventRemarkTest {

  private static final String EVENT_REMARKS_STRING = "A remark about the event";

  private final EventRemark eventRemark = new EventRemark();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:eventRemarks", EVENT_REMARKS_STRING);

    // When
    var result = eventRemark.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(EVENT_REMARKS_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/notes/value", EVENT_REMARKS_STRING);

    // When
    var result = eventRemark.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(EVENT_REMARKS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = eventRemark.getTerm();

    // Then
    assertThat(result).isEqualTo(EventRemark.TERM);
  }

}
