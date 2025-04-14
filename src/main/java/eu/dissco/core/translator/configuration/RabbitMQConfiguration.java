package eu.dissco.core.translator.configuration;

import eu.dissco.core.translator.component.MessageCompressionComponent;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

  @Bean("compressed")
  public RabbitTemplate compressedTemplate(ConnectionFactory connectionFactory,
      MessageCompressionComponent compressedMessageConverter) {
    final RabbitTemplate rabbitTemplate =
        new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(compressedMessageConverter);
    return rabbitTemplate;
  }
}
