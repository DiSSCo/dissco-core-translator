package eu.dissco.core.translator.service;

import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.properties.RabbitMqProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

@Service
@Slf4j
@AllArgsConstructor
public class RabbitMqService {

  private final JsonMapper mapper;
  private final RabbitTemplate rabbitTemplate;
  private final RabbitMqProperties rabbitMqProperties;

  public void sendMessage(DigitalSpecimenEvent event) {
    rabbitTemplate.convertAndSend(rabbitMqProperties.getExchangeName(),
        rabbitMqProperties.getRoutingKeyName(), mapper.writeValueAsString(event));
  }
}
