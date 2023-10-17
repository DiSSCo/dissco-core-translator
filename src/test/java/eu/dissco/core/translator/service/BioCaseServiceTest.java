package eu.dissco.core.translator.service;

import static eu.dissco.core.translator.TestUtils.givenDigitalSpecimen;
import static eu.dissco.core.translator.TestUtils.loadResourceFile;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.properties.EnrichmentProperties;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.SourceSystemRepository;
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
  private SourceSystemRepository repository;
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
    service = new BioCaseService(mapper, properties, webClient, repository, configuration, factory,
        kafkaService, enrichmentProperties, digitalSpecimenDirector, fdoProperties);

    // Given
    givenJsonWebclient();
  }

  @Test
  void testRetrieveData206() throws Exception {
    // Given
    given(properties.getSourceSystemId()).willReturn("ABC-DDD-ASD");
    given(repository.getEndpoint(anyString())).willReturn("https://endpoint.com");
    given(responseSpec.bodyToMono(any(Class.class))).willReturn(
            Mono.just(loadResourceFile("biocase/geocase-record-dropped.xml")))
        .willReturn(Mono.just(loadResourceFile("biocase/biocase-206-response.xml")));
    given(properties.getItemsPerRequest()).willReturn(100);
    given(fdoProperties.getDigitalSpecimenType()).willReturn("Doi of the digital specimen");
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(any(JsonNode.class), anyBoolean()))
        .willReturn(givenDigitalSpecimen());

    // When
    service.retrieveData();

    // Then
    then(webClient).should(times(2)).get();
    then(kafkaService).should(times(99)).sendMessage(eq("digital-specimen"), anyString());
  }

  @Test
  void testRetrieveDataWithMedia206() throws Exception {
    // Given
    given(properties.getSourceSystemId()).willReturn("ABC-DDD-ASD");
    given(repository.getEndpoint(anyString())).willReturn("https://endpoint.com");
    given(responseSpec.bodyToMono(any(Class.class))).willReturn(
        Mono.just(loadResourceFile("biocase/biocase-206-with-media.xml")));
    given(properties.getItemsPerRequest()).willReturn(101);
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(any(JsonNode.class), anyBoolean()))
        .willReturn(givenDigitalSpecimen());
    given(fdoProperties.getDigitalSpecimenType()).willReturn("Doi of the digital specimen");
    given(fdoProperties.getDigitalMediaObjectType()).willReturn("Doi of the digital media object");

    // When
    service.retrieveData();

    // Then
    then(webClient).should(times(1)).get();
    then(kafkaService).should(times(100)).sendMessage(eq("digital-specimen"), anyString());
  }

  @ParameterizedTest
  @MethodSource("eu.dissco.core.translator.TestUtils#provideInvalidDigitalSpecimen")
  void testRetrieveDataInvalidSpecimen(DigitalSpecimen digitalSpecimen) throws Exception {
    // Given
    given(properties.getSourceSystemId()).willReturn("ABC-DDD-ASD");
    given(repository.getEndpoint(anyString())).willReturn("https://endpoint.com");
    given(responseSpec.bodyToMono(any(Class.class))).willReturn(
        Mono.just(loadResourceFile("biocase/biocase-206-with-media.xml")));
    given(properties.getItemsPerRequest()).willReturn(101);
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(any(JsonNode.class), anyBoolean()))
        .willReturn(digitalSpecimen);

    // When
    service.retrieveData();

    // Then
    then(webClient).should(times(1)).get();
    then(kafkaService).shouldHaveNoInteractions();
  }

  private void givenJsonWebclient() {
    given(webClient.get()).willReturn(headersSpec);
    given(headersSpec.uri(anyString())).willReturn(uriSpec);
    given(uriSpec.retrieve()).willReturn(responseSpec);
  }


}
