package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordNumberTest {

  private static final String RECORD_NUMBER_STRING = "OPP 7101";

  private final RecordNumber recordNumber = new RecordNumber();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:recordNumber", RECORD_NUMBER_STRING);

    // When
    var result = recordNumber.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(RECORD_NUMBER_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:collectorsFieldNumber", RECORD_NUMBER_STRING);

    // When
    var result = recordNumber.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(RECORD_NUMBER_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = recordNumber.getTerm();

    // Then
    assertThat(result).isEqualTo(RecordNumber.TERM);
  }

}
