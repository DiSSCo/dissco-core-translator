package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerbatimEventDateTest {

  private static final String VERBATIM_EVENT_DATE_STRING = "spring 1910";

  private final VerbatimEventDate verbatimEventDate = new VerbatimEventDate();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verbatimEventDate", VERBATIM_EVENT_DATE_STRING);

    // When
    var result = verbatimEventDate.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM_EVENT_DATE_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/dateTime/dateText", VERBATIM_EVENT_DATE_STRING);

    // When
    var result = verbatimEventDate.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM_EVENT_DATE_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = verbatimEventDate.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimEventDate.TERM);
  }

}
