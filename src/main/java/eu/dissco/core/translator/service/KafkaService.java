package eu.dissco.core.translator.service;

import eu.dissco.core.translator.properties.KafkaProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaService {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendMessage(String topic, String event) {
    ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, event);
    future.addCallback(new ListenableFutureCallback<>() {

      @Override
      public void onSuccess(SendResult<String, String> result) {
        // No need to do anything on success
      }

      @Override
      public void onFailure(Throwable ex) {
        log.error("Unable to send message: {}", event, ex);
      }
    });
  }
}
