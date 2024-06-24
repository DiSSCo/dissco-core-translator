package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OccurrenceRemarksTest {

  private static final String OCCURRENCE_REMARKS_STRING = "Remarks about the event";

  private final OccurrenceRemarks occurrenceRemarks = new OccurrenceRemarks();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:occurrenceRemarks", OCCURRENCE_REMARKS_STRING);

    // When
    var result = occurrenceRemarks.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(OCCURRENCE_REMARKS_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:notes/value", OCCURRENCE_REMARKS_STRING);

    // When
    var result = occurrenceRemarks.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(OCCURRENCE_REMARKS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = occurrenceRemarks.getTerm();

    // Then
    assertThat(result).isEqualTo(OccurrenceRemarks.TERM);
  }

}
