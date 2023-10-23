package eu.dissco.core.translator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import java.util.concurrent.CompletableFuture;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaService {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper mapper;

  public void sendMessage(String topic, DigitalSpecimenEvent event) throws JsonProcessingException {
    CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic,
        mapper.writeValueAsString(event));
    future.whenComplete((result, ex) -> {
      if (ex != null) {
        log.error("Unable to send message: {}", event, ex);
      }
    });
  }
}
