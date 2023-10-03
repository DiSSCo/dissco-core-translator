package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.schema.DigitalSpecimen.OdsTopicOrigin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TopicOriginTest {

  private static final OdsTopicOrigin TOPIC_ORIGIN_STRING = OdsTopicOrigin.NATURAL;
  private final TopicOrigin topicOrigin = new TopicOrigin();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var ds = new eu.dissco.core.translator.schema.DigitalSpecimen();

    // When
    var result = topicOrigin.calculate(ds);

    // Then
    assertThat(result).isEqualTo(TOPIC_ORIGIN_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = topicOrigin.getTerm();

    // Then
    assertThat(result).isEqualTo(TopicOrigin.TERM);
  }
}
