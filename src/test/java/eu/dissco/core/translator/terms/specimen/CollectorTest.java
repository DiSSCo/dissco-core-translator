package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CollectorTest {

  private static final String COLLECTOR_NAME = "Fricke, Ronald";
  private final Collector collector = new Collector();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("dwc:recordedBy", COLLECTOR_NAME);

    // When
    var result = collector.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(COLLECTOR_NAME);
  }

  @Test
  void testRetrieveFromABCDMultipleAgents() {
    // Given
    var collectorName2 = "Troschel, Hans-Julius";
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:gathering/agents/gatheringAgent/0/person/fullName", COLLECTOR_NAME);
    unit.put("abcd:gathering/agents/gatheringAgent/0/person/agentText", "Tom");
    unit.put("abcd:gathering/agents/gatheringAgent/1/person/agentText", collectorName2);
    unit.put("abcd:gathering/agents/gatheringAgentsText", "Sam Leeflang");

    // When
    var result = collector.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(COLLECTOR_NAME + " | " + collectorName2);
  }

  @Test
  void testRetrieveFromABCDGatheringAgentsText() {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put("abcd:gathering/agents/gatheringAgentsText", COLLECTOR_NAME);

    // When
    var result = collector.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(COLLECTOR_NAME);
  }

  @Test
  void testRetrieveFromABCDEmpty() {
    // Given
    var unit = new ObjectMapper().createObjectNode();

    // When
    var result = collector.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = collector.getTerm();

    // Then
    assertThat(result).isEqualTo(Collector.TERM);
  }

}
