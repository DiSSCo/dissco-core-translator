package eu.dissco.core.translator.service;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.NORMALISED_PHYSICAL_SPECIMEN_ID;
import static eu.dissco.core.translator.TestUtils.givenDigitalMedia;
import static eu.dissco.core.translator.TestUtils.givenDigitalSpecimen;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.dissco.core.translator.domain.DigitalMediaEvent;
import eu.dissco.core.translator.domain.DigitalMediaWrapper;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.domain.DigitalSpecimenWrapper;
import eu.dissco.core.translator.properties.RabbitMqProperties;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class RabbitMqServiceTest {

  private RabbitMQContainer container;
  private RabbitMqService rabbitMqService;
  private RabbitTemplate rabbitTemplate;

  @BeforeEach
  void setup() throws IOException, InterruptedException {
    container = new RabbitMQContainer("rabbitmq:4.0.8-management-alpine");
    container.start();
    // Create the exchange, queue and binding
    container.execInContainer("rabbitmqadmin", "declare", "exchange", "name=nu-search-exchange",
        "type=direct", "durable=true");
    container.execInContainer("rabbitmqadmin", "declare", "queue", "name=nu-search-queue",
        "queue_type=quorum", "durable=true");
    container.execInContainer("rabbitmqadmin", "declare", "binding", "source=nu-search-exchange",
        "destination_type=queue", "destination=nu-search-queue", "routing_key=nu-search");

    CachingConnectionFactory factory = new CachingConnectionFactory(container.getHost());
    factory.setPort(container.getAmqpPort());
    factory.setUsername(container.getAdminUsername());
    factory.setPassword(container.getAdminPassword());
    rabbitTemplate = new RabbitTemplate(factory);
    rabbitTemplate.setReceiveTimeout(100L);
    rabbitMqService = new RabbitMqService(MAPPER, rabbitTemplate, new RabbitMqProperties());
  }

  @AfterEach
  void shutdown() {
    container.stop();
  }

  @Test
  void testSendMessage() throws JsonProcessingException {
    // Given
    var message = new DigitalSpecimenEvent(List.of(), new DigitalSpecimenWrapper(
        NORMALISED_PHYSICAL_SPECIMEN_ID, "https://doi.org/21.T11148/894b1e6cad57e921764e",
        givenDigitalSpecimen(), MAPPER.createObjectNode()),
        List.of(new DigitalMediaEvent(List.of(),
            new DigitalMediaWrapper("https://doi.org/21.T11148/bbad8c4e101e8af01115",
                givenDigitalMedia(), MAPPER.createObjectNode()))));

    // When
    rabbitMqService.sendMessage(message);

    // Then
    var string = new String(rabbitTemplate.receive("nu-search-queue").getBody());
    assertThat(string).isEqualTo(MAPPER.writeValueAsString(message));
  }

}
