package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DateCollectedTest {

  private static final String DATE = "1924-11-20";

  private final DateCollected dateCollected = new DateCollected();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("dwc:eventDate", DATE);

    // When
    var result = dateCollected.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(DATE);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:gathering/dateTime/isodateTimeStart", DATE);

    // When
    var result = dateCollected.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(DATE);
  }

  @Test
  void testGetTerm() {
    // When
    var result = dateCollected.getTerm();

    // Then
    assertThat(result).isEqualTo(DateCollected.TERM);
  }

}
