package eu.dissco.core.translator.service;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.NORMALISED_PHYSICAL_SPECIMEN_ID;
import static eu.dissco.core.translator.TestUtils.givenDigitalMediaObjects;
import static eu.dissco.core.translator.TestUtils.givenDigitalSpecimen;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.dissco.core.translator.domain.DigitalMediaObject;
import eu.dissco.core.translator.domain.DigitalMediaObjectEvent;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.domain.DigitalSpecimenWrapper;
import java.util.List;
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
    this.service = new KafkaService(kafkaTemplate, MAPPER);
  }

  @Test
  void testSendMessage() throws JsonProcessingException {
    // Given
    var x = CompletableFuture.completedFuture(sendResult);
    given(kafkaTemplate.send(anyString(), anyString())).willReturn(x);
    var digitalSpecimenEvent = givenDigitalSpecimenEvent();

    // When
    service.sendMessage("test-topic", digitalSpecimenEvent);

    // Then
    then(kafkaTemplate).should()
        .send("test-topic", MAPPER.writeValueAsString(digitalSpecimenEvent));
  }

  private DigitalSpecimenEvent givenDigitalSpecimenEvent() {
    return new DigitalSpecimenEvent(List.of("AAS"),
        new DigitalSpecimenWrapper(NORMALISED_PHYSICAL_SPECIMEN_ID,
            "https://doi.org/21.T11148/894b1e6cad57e921764e", givenDigitalSpecimen(), null),
        List.of(new DigitalMediaObjectEvent(List.of(),
            new DigitalMediaObject("https://doi.org/21.T11148/bbad8c4e101e8af01115",
                NORMALISED_PHYSICAL_SPECIMEN_ID, givenDigitalMediaObjects(), null))));
  }

}
