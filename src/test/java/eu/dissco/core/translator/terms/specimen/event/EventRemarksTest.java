package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventRemarksTest {

  private static final String EVENT_REMARKS_STRING = "Remarks about the event";

  private final EventRemarks eventRemarks = new EventRemarks();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:eventRemarks", EVENT_REMARKS_STRING);

    // When
    var result = eventRemarks.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(EVENT_REMARKS_STRING);
  }

  @Test
  void testRetrieveFromDWCABoth() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:eventRemarks", EVENT_REMARKS_STRING);
    unit.put("dwc:occurrenceRemarks", "an occurrence remark");

    // When
    var result = eventRemarks.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(EVENT_REMARKS_STRING + " | an occurrence remark");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:notes/value", EVENT_REMARKS_STRING);

    // When
    var result = eventRemarks.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(EVENT_REMARKS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = eventRemarks.getTerm();

    // Then
    assertThat(result).isEqualTo(EventRemarks.TERM);
  }

}
