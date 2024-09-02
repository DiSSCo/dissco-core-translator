package eu.dissco.core.translator.service;

import static eu.dissco.core.translator.TestUtils.givenDigitalMedia;
import static eu.dissco.core.translator.TestUtils.givenDigitalSpecimen;
import static eu.dissco.core.translator.TestUtils.loadResourceFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.component.SourceSystemComponent;
import eu.dissco.core.translator.database.jooq.enums.JobState;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.domain.TranslatorJobResult;
import eu.dissco.core.translator.properties.EnrichmentProperties;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.schema.DigitalMedia;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.terms.BaseDigitalObjectDirector;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import java.io.IOException;
import javax.xml.stream.XMLInputFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class BioCaseServiceTest {

  private final XMLInputFactory factory = XMLInputFactory.newFactory();
  private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
  @Mock
  private WebClient webClient;
  @Mock
  private WebClientProperties properties;
  @Mock
  private SourceSystemComponent sourceSystemComponent;
  @Mock
  private RequestHeadersUriSpec headersSpec;
  @Mock
  private RequestHeadersSpec uriSpec;
  @Mock
  private ResponseSpec responseSpec;
  @Mock
  private KafkaService kafkaService;
  @Mock
  private EnrichmentProperties enrichmentProperties;
  @Mock
  private BaseDigitalObjectDirector digitalSpecimenDirector;
  @Mock
  private FdoProperties fdoProperties;
  private BioCaseService service;

  @BeforeEach
  void setup() throws IOException {
    var configuration = new Configuration(Configuration.VERSION_2_3_31);
    configuration.setTemplateLoader(
        new FileTemplateLoader(new ClassPathResource("templates").getFile()));
    service = new BioCaseService(mapper, properties, webClient, sourceSystemComponent, configuration, factory,
        kafkaService, enrichmentProperties, digitalSpecimenDirector, fdoProperties);

    // Given
    givenJsonWebclient();
  }

  @Test
  void testRetrieveData206() throws Exception {
    // Given
    var expectedResult = new TranslatorJobResult(JobState.COMPLETED, 99);
    given(sourceSystemComponent.getSourceSystemEndpoint()).willReturn("https://endpoint.com");
    given(responseSpec.bodyToMono(any(Class.class))).willReturn(
            Mono.just(loadResourceFile("biocase/geocase-record-dropped.xml")))
        .willReturn(Mono.just(loadResourceFile("biocase/biocase-206-response.xml")));
    given(properties.getItemsPerRequest()).willReturn(100);
    given(fdoProperties.getDigitalSpecimenType()).willReturn("Doi of the digital specimen");
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(any(JsonNode.class), anyBoolean()))
        .willReturn(givenDigitalSpecimen());

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expectedResult);
    then(webClient).should(times(2)).get();
    then(kafkaService).should(times(99)).sendMessage(any(
        DigitalSpecimenEvent.class));
  }

  @Test
  void testRetrieveDataWithMedia206() throws Exception {
    // Given
    var expectedResult = new TranslatorJobResult(JobState.COMPLETED, 100);
    given(sourceSystemComponent.getSourceSystemEndpoint()).willReturn("https://endpoint.com");
    given(responseSpec.bodyToMono(any(Class.class))).willReturn(
        Mono.just(loadResourceFile("biocase/biocase-206-with-media.xml")));
    given(properties.getItemsPerRequest()).willReturn(101);
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(any(JsonNode.class), anyBoolean()))
        .willReturn(givenDigitalSpecimen());
    given(fdoProperties.getDigitalSpecimenType()).willReturn("Doi of the digital specimen");
    given(fdoProperties.getDigitalMediaType()).willReturn("Doi of the digital media");
    given(digitalSpecimenDirector.assembleDigitalMedia(anyBoolean(), any(JsonNode.class),
        anyString()))
        .willReturn(givenDigitalMedia());

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expectedResult);
    then(webClient).should(times(1)).get();
    then(kafkaService).should(times(100)).sendMessage(any(
        DigitalSpecimenEvent.class));
  }

  @Test
  void testRetrieveDataInvalidMedia() throws Exception {
    // Given
    var expectedResult = new TranslatorJobResult(JobState.COMPLETED, 1);
    given(sourceSystemComponent.getSourceSystemEndpoint()).willReturn("https://endpoint.com");
    given(responseSpec.bodyToMono(any(Class.class))).willReturn(
        Mono.just(loadResourceFile("biocase/biocase-206-with-invalid-media.xml")));
    given(properties.getItemsPerRequest()).willReturn(101);
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(any(JsonNode.class), anyBoolean()))
        .willReturn(givenDigitalSpecimen());
    given(fdoProperties.getDigitalSpecimenType()).willReturn("Doi of the digital specimen");
    given(digitalSpecimenDirector.assembleDigitalMedia(anyBoolean(), any(JsonNode.class),
        anyString())).willReturn(new DigitalMedia());

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expectedResult);
    var captor = ArgumentCaptor.forClass(DigitalSpecimenEvent.class);
    then(webClient).should(times(1)).get();
    then(kafkaService).should(times(1)).sendMessage(captor.capture());
    assertThat(captor.getValue().digitalMediaEvents()).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("eu.dissco.core.translator.TestUtils#provideInvalidDigitalSpecimen")
  void testRetrieveDataInvalidSpecimen(DigitalSpecimen digitalSpecimen) throws Exception {
    // Given
    var expectedResult = new TranslatorJobResult(JobState.FAILED, 0);
    given(sourceSystemComponent.getSourceSystemEndpoint()).willReturn("https://endpoint.com");
    given(responseSpec.bodyToMono(any(Class.class))).willReturn(
        Mono.just(loadResourceFile("biocase/biocase-206-with-media.xml")));
    given(properties.getItemsPerRequest()).willReturn(101);
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(any(JsonNode.class), anyBoolean()))
        .willReturn(digitalSpecimen);

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expectedResult);
    then(webClient).should(times(1)).get();
    then(kafkaService).shouldHaveNoInteractions();
  }

  private void givenJsonWebclient() {
    given(webClient.get()).willReturn(headersSpec);
    given(headersSpec.uri(anyString())).willReturn(uriSpec);
    given(uriSpec.retrieve()).willReturn(responseSpec);
  }


}
