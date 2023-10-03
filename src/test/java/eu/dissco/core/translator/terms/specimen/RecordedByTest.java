package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordedByTest {

  private static final String RECORDED_BY_STRING = "Fricke, Ronald";
  private final RecordedBy recordedBy = new RecordedBy();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:recordedBy", RECORDED_BY_STRING);

    // When
    var result = recordedBy.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(RECORDED_BY_STRING);
  }

  @Test
  void testRetrieveFromABCDMultipleAgents() {
    // Given
    var collectorName2 = "Troschel, Hans-Julius";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/agents/gatheringAgent/0/person/fullName", RECORDED_BY_STRING);
    unit.put("abcd:gathering/agents/gatheringAgent/0/person/agentText", "Tom");
    unit.put("abcd:gathering/agents/gatheringAgent/1/person/agentText", collectorName2);
    unit.put("abcd:gathering/agents/gatheringAgentsText", "Sam Leeflang");

    // When
    var result = recordedBy.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(RECORDED_BY_STRING + " | " + collectorName2);
  }

  @Test
  void testRetrieveFromABCDGatheringAgentsText() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/agents/gatheringAgentsText/value", RECORDED_BY_STRING);

    // When
    var result = recordedBy.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(RECORDED_BY_STRING);
  }

  @Test
  void testRetrieveFromABCDEmpty() {
    // Given
    var unit = MAPPER.createObjectNode();

    // When
    var result = recordedBy.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = recordedBy.getTerm();

    // Then
    assertThat(result).isEqualTo(RecordedBy.TERM);
  }

}
