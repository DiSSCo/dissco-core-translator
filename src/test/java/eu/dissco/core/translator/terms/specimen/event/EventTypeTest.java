package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventTypeTest {

  private final EventType eventType = new EventType();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:eventType", "Sampling");

    // When
    var result = eventType.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Sampling");
  }

  @Test
  void testRetrieveFromDWCANoValue() {
    // Given
    var unit = MAPPER.createObjectNode();

    // When
    var result = eventType.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Collecting Event");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();

    // When
    var result = eventType.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Collecting Event");
  }

  @Test
  void testGetTerm() {
    // When
    var result = eventType.getTerm();

    // Then
    assertThat(result).isEqualTo(EventType.TERM);
  }

}
