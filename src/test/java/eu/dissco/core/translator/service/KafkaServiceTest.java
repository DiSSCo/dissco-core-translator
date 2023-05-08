package eu.dissco.core.translator.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

@ExtendWith(MockitoExtension.class)
class KafkaServiceTest {

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;
  @Mock
  private SendResult<String, String> sendResult;
  private KafkaService service;

  @BeforeEach
  void setup() {
    this.service = new KafkaService(kafkaTemplate);
  }

  @Test
  void testSendMessage() {
    // Given
    var x = CompletableFuture.completedFuture(sendResult);
    given(kafkaTemplate.send(anyString(), anyString())).willReturn(x);

    // When
    service.sendMessage("test-topic", "Test Message");

    // Then
    then(kafkaTemplate).should().send(anyString(), anyString());
  }

}
