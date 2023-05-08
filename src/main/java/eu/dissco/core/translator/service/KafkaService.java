package eu.dissco.core.translator.service;

import java.util.concurrent.CompletableFuture;
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
    CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, event);
    future.whenComplete((result, ex) -> {
      if (ex != null){
        log.error("Unable to send message: {}", event, ex);
      }
    });
  }
}
