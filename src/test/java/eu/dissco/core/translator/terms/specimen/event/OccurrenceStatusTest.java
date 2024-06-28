package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OccurrenceStatusTest {

  private static final String OCCURRENCE_STATUS_STRING = "Present";

  private final OccurrenceStatus occurrenceStatus = new OccurrenceStatus();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:occurrenceStatus", OCCURRENCE_STATUS_STRING);

    // When
    var result = occurrenceStatus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(OCCURRENCE_STATUS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = occurrenceStatus.getTerm();

    // Then
    assertThat(result).isEqualTo(OccurrenceStatus.TERM);
  }

}
