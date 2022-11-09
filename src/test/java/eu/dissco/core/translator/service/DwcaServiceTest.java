package eu.dissco.core.translator.service;

import static eu.dissco.core.translator.TestUtils.DWC_DEFAULTS;
import static eu.dissco.core.translator.TestUtils.DWC_FIELD_MAPPING;
import static eu.dissco.core.translator.TestUtils.DWC_KEW_DEFAULTS;
import static eu.dissco.core.translator.TestUtils.DWC_KEW_FIELD_MAPPING;
import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import eu.dissco.core.translator.component.MappingComponent;
import eu.dissco.core.translator.properties.DwcaProperties;
import eu.dissco.core.translator.properties.EnrichmentProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.repository.SourceSystemRepository;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

@ExtendWith(MockitoExtension.class)
class DwcaServiceTest {

  @Mock
  private WebClientProperties webClientProperties;
  @Mock
  private WebClient webClient;
  @Mock
  private DwcaProperties dwcaProperties;
  @Mock
  private RequestHeadersUriSpec headersSpec;
  @Mock
  private RequestHeadersSpec uriSpec;
  @Mock
  private ResponseSpec responseSpec;
  @Mock
  private KafkaService kafkaService;
  @Mock
  private MappingComponent mappingComponent;
  @Mock
  private EnrichmentProperties enrichmentProperties;
  @Mock
  private SourceSystemRepository repository;


  private DwcaService service;

  private static void cleanup(String first) throws IOException {
    FileSystemUtils.deleteRecursively(Path.of("src/test/resources/dwca/test/temp"));
    Files.delete(Path.of(first));
  }

  @BeforeEach
  void setup() {
    this.service = new DwcaService(MAPPER, webClientProperties, webClient, dwcaProperties,
        kafkaService, mappingComponent, enrichmentProperties, repository);
  }

  @Test
  void testRetrieveData() throws IOException {
    // Given
    given(mappingComponent.getFieldMappings()).willReturn(DWC_FIELD_MAPPING);
    given(mappingComponent.getDefaultMappings()).willReturn(DWC_DEFAULTS);
    givenEndpoint();
    givenDWCA("/dwca-rbins.zip");

    // When
    service.retrieveData();

    // Then
    then(kafkaService).should(times(9)).sendMessage(eq("digital-specimen"), anyString());
    then(kafkaService).should(times(0)).sendMessage(eq("digital-media-object"), anyString());
    cleanup("src/test/resources/dwca/test/dwca-rbins.zip");
  }

  @Test
  void testRetrieveDataWithGbifMedia() throws IOException {
    // Given
    given(mappingComponent.getFieldMappings()).willReturn(DWC_KEW_FIELD_MAPPING);
    given(mappingComponent.getDefaultMappings()).willReturn(DWC_KEW_DEFAULTS);
    givenEndpoint();
    givenDWCA("/dwca-kew-gbif-media.zip");

    // When
    service.retrieveData();

    // Then
    then(kafkaService).should(times(19)).sendMessage(eq("digital-specimen"), anyString());
    then(kafkaService).should(times(19)).sendMessage(eq("digital-media-object"), anyString());
    cleanup("src/test/resources/dwca/test/dwca-kew-gbif-media.zip");
  }

  @Test
  void testRetrieveDataWithAcMedia() throws IOException {
    // Given
    given(mappingComponent.getFieldMappings()).willReturn(DWC_KEW_FIELD_MAPPING);
    given(mappingComponent.getDefaultMappings()).willReturn(DWC_KEW_DEFAULTS);
    givenEndpoint();
    givenDWCA("/dwca-naturalis-ac-media.zip");

    // When
    service.retrieveData();

    // Then
    then(kafkaService).should(times(14)).sendMessage(eq("digital-specimen"), anyString());
    then(kafkaService).should(times(14)).sendMessage(eq("digital-media-object"), anyString());
    cleanup("src/test/resources/dwca/test/dwca-naturalis-ac-media.zip");
  }

  @Test
  void testRetrieveDataWithAssociatedMedia() throws IOException {
    // Given
    given(mappingComponent.getFieldMappings()).willReturn(DWC_KEW_FIELD_MAPPING);
    given(mappingComponent.getDefaultMappings()).willReturn(DWC_KEW_DEFAULTS);
    givenEndpoint();
    givenDWCA("/dwca-lux-associated-media.zip");

    // When
    service.retrieveData();

    // Then
    then(kafkaService).should(times(20)).sendMessage(eq("digital-specimen"), anyString());
    then(kafkaService).should(times(21)).sendMessage(eq("digital-media-object"), anyString());
    cleanup("src/test/resources/dwca/test/dwca-lux-associated-media.zip");
  }

  private void givenEndpoint() {
    given(webClientProperties.getSourceSystemId()).willReturn("ABC-DDD-ASD");
    given(repository.getEndpoint(anyString())).willReturn("https://endpoint.com");
  }

  private void givenDWCA(String file) {
    given(dwcaProperties.getDownloadFile())
        .willReturn(getAbsolutePath() + file);
    given(dwcaProperties.getTempFolder())
        .willReturn(getAbsolutePath() + "/temp");
    given(webClient.get()).willReturn(headersSpec);
    given(headersSpec.uri(any(URI.class))).willReturn(uriSpec);
    given(uriSpec.retrieve()).willReturn(responseSpec);
    given(responseSpec.bodyToFlux(DataBuffer.class)).willReturn(
        DataBufferUtils.read(new ClassPathResource("dwca/" + file),
            new DefaultDataBufferFactory(), 1000));
  }


  private String getAbsolutePath() {
    String path = "src/test/resources/dwca/test";
    File file = new File(path);
    return file.getAbsolutePath();
  }

}
