package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FieldNotesTest {

  private static final String FIELD_NOTES_STRING = "Collected while raining";

  private final FieldNotes fieldNotes = new FieldNotes();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:fieldNotes", FIELD_NOTES_STRING);

    // When
    var result = fieldNotes.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(FIELD_NOTES_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/fieldNotes/value", FIELD_NOTES_STRING);

    // When
    var result = fieldNotes.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(FIELD_NOTES_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = fieldNotes.getTerm();

    // Then
    assertThat(result).isEqualTo(FieldNotes.TERM);
  }

}
