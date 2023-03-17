package eu.dissco.core.translator.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doAnswer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@ExtendWith(MockitoExtension.class)
class KafkaServiceTest {

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;
  @Mock
  private ListenableFuture<SendResult<String, String>> listenableFuture;
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
    given(kafkaTemplate.send(anyString(), anyString())).willReturn(listenableFuture);
    doAnswer(invocation -> {
      ListenableFutureCallback callBack = invocation.getArgument(0);
      callBack.onSuccess(sendResult);
      return null;
    }).when(listenableFuture).addCallback(any(ListenableFutureCallback.class));

    // When
    service.sendMessage("test-topic", "Test Message");

    // Then
    then(kafkaTemplate).should().send(anyString(), anyString());
  }

  @Test
  void testSendFailure() {
    // Given
    given(kafkaTemplate.send(anyString(), anyString())).willReturn(listenableFuture);
    doAnswer(invocation -> {
      ListenableFutureCallback callBack = invocation.getArgument(0);
      callBack.onFailure(new KafkaException("Send failed", new Exception()));
      return null;
    }).when(listenableFuture).addCallback(any(ListenableFutureCallback.class));

    // When
    service.sendMessage("test-topic", "Test Message");

    // Then
    then(kafkaTemplate).should().send(anyString(), anyString());
  }
}
