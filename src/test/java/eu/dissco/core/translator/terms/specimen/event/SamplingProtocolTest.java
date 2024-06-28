package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SamplingProtocolTest {

  private static final String SAMPLING_PROTOCOL_STRING = "bottom trawl";

  private final SamplingProtocol samplingProtocol = new SamplingProtocol();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:samplingProtocol", SAMPLING_PROTOCOL_STRING);

    // When
    var result = samplingProtocol.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(SAMPLING_PROTOCOL_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/method/value", SAMPLING_PROTOCOL_STRING);

    // When
    var result = samplingProtocol.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(SAMPLING_PROTOCOL_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = samplingProtocol.getTerm();

    // Then
    assertThat(result).isEqualTo(SamplingProtocol.TERM);
  }

}
