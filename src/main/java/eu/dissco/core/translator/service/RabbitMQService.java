package eu.dissco.core.translator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.properties.RabbitMQProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class RabbitMQService {

  private final ObjectMapper mapper;
  private final RabbitTemplate rabbitTemplate;
  private final RabbitMQProperties rabbitMQProperties;

  public void sendMessage(DigitalSpecimenEvent event) throws JsonProcessingException {
    rabbitTemplate.convertAndSend(rabbitMQProperties.getExchangeName(),
        rabbitMQProperties.getRoutingKeyName(), mapper.writeValueAsString(event));
  }
}
