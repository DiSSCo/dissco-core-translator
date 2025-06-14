package eu.dissco.core.translator.service;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static eu.dissco.core.translator.TestUtils.givenDigitalMedia;
import static eu.dissco.core.translator.TestUtils.givenDigitalSpecimen;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.dissco.core.translator.TestUtils;
import eu.dissco.core.translator.component.SourceSystemComponent;
import eu.dissco.core.translator.database.jooq.enums.JobState;
import eu.dissco.core.translator.domain.DigitalSpecimenEvent;
import eu.dissco.core.translator.domain.TranslatorJobResult;
import eu.dissco.core.translator.properties.ApplicationProperties;
import eu.dissco.core.translator.properties.DwcaProperties;
import eu.dissco.core.translator.properties.EnrichmentProperties;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.repository.DwcaRepository;
import eu.dissco.core.translator.schema.DigitalMedia;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.terms.BaseDigitalObjectDirector;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import org.gbif.dwc.terms.Term;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
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

  private final XMLInputFactory factory = XMLInputFactory.newFactory();
  private final ApplicationProperties applicationProperties = new ApplicationProperties();
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
  private RabbitMqService rabbitMqService;
  @Mock
  private EnrichmentProperties enrichmentProperties;
  @Mock
  private SourceSystemComponent sourceSystemComponent;
  @Mock
  private DwcaRepository dwcaRepository;
  @Mock
  private BaseDigitalObjectDirector digitalSpecimenDirector;
  @Mock
  private FdoProperties fdoProperties;
  private DwcaService service;

  private static void cleanup(String first) throws IOException {
    FileSystemUtils.deleteRecursively(Path.of("src/test/resources/dwca/test/temp"));
    Files.delete(Path.of(first));
  }

  @BeforeEach
  void setup() {
    this.service = new DwcaService(MAPPER, webClient, dwcaProperties,
        rabbitMqService, enrichmentProperties, sourceSystemComponent, dwcaRepository,
        digitalSpecimenDirector, fdoProperties, applicationProperties, factory);

    // Given
    givenSourceSystem();
  }

  @ParameterizedTest
  @ValueSource(ints = {1, 6, 9})
  @NullSource
  void testRetrieveData(Integer processedRecords) throws Exception {
    // Given
    applicationProperties.setMaxItems(processedRecords);
    if (processedRecords == null) {
      processedRecords = 9;
    }
    var expected = new TranslatorJobResult(JobState.COMPLETED, processedRecords);
    var captor = ArgumentCaptor.forClass(JsonNode.class);
    givenDWCA("/dwca-rbins.zip");
    given(dwcaRepository.getCoreRecords(anyList(), anyString())).willReturn(givenSpecimenMap(9));
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(captor.capture(), anyBoolean()))
        .willReturn(givenDigitalSpecimen());
    given(fdoProperties.getDigitalSpecimenType()).willReturn("Doi of the digital specimen");

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expected);
    then(dwcaRepository).should(times(2)).createTable(anyString(), any(Term.class));
    then(dwcaRepository).should(times(2)).postRecords(anyString(), any(Term.class), anyList());
    then(sourceSystemComponent).should().storeEmlRecord(any(File.class));
    then(rabbitMqService).should(times(processedRecords)).sendMessage(any(
        DigitalSpecimenEvent.class));
    assertThat(captor.getValue().get("eml:license").asText()).isEqualTo(
        "http://creativecommons.org/licenses/by-nc/4.0/legalcode");
    assertThat(captor.getValue().get("eml:title").asText()).isEqualTo(
        "Royal Belgian Institute of Natural Sciences Crustacea collection");
    cleanup("src/test/resources/dwca/test/dwca-rbins.zip");
  }

  @Test
  void testRetrieveDataEmlException() throws Exception {
    // Given
    var expected = new TranslatorJobResult(JobState.COMPLETED, 9);
    var captor = ArgumentCaptor.forClass(JsonNode.class);
    givenDWCA("/dwca-rbins-invalid-eml.zip");
    given(dwcaRepository.getCoreRecords(anyList(), anyString())).willReturn(givenSpecimenMap(9));
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(captor.capture(), anyBoolean()))
        .willReturn(givenDigitalSpecimen());
    given(fdoProperties.getDigitalSpecimenType()).willReturn("Doi of the digital specimen");

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expected);
    then(dwcaRepository).should(times(2)).createTable(anyString(), any(Term.class));
    then(dwcaRepository).should(times(2)).postRecords(anyString(), any(Term.class), anyList());
    then(rabbitMqService).should(times(9)).sendMessage(any(
        DigitalSpecimenEvent.class));
    assertThat(captor.getValue().get("eml:license")).isNull();
    assertThat(captor.getValue().get("eml:title")).isNull();
    cleanup("src/test/resources/dwca/test/dwca-rbins-invalid-eml.zip");
  }

  @Test
  void testRetrieveDataWithLicenseText() throws Exception {
    // Given
    var expected = new TranslatorJobResult(JobState.COMPLETED, 9);
    var captor = ArgumentCaptor.forClass(JsonNode.class);
    givenDWCA("/dwca-rbins-license-text.zip");
    given(dwcaRepository.getCoreRecords(anyList(), anyString())).willReturn(givenSpecimenMap(9));
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(captor.capture(), anyBoolean()))
        .willReturn(givenDigitalSpecimen());
    given(fdoProperties.getDigitalSpecimenType()).willReturn("Doi of the digital specimen");

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expected);
    then(dwcaRepository).should(times(2)).createTable(anyString(), any(Term.class));
    then(dwcaRepository).should(times(2)).postRecords(anyString(), any(Term.class), anyList());
    then(sourceSystemComponent).should().storeEmlRecord(any(File.class));
    then(rabbitMqService).should(times(9)).sendMessage(any(
        DigitalSpecimenEvent.class));
    assertThat(captor.getValue().get("eml:license").asText()).isEqualTo(
        "Creative Commons Attribution Non Commercial (CC-BY-NC) 4.0 License");
    assertThat(captor.getValue().get("eml:title").asText()).isEqualTo(
        "Royal Belgian Institute of Natural Sciences Crustacea collection");
    cleanup("src/test/resources/dwca/test/dwca-rbins-license-text.zip");
  }

  @ParameterizedTest
  @MethodSource("eu.dissco.core.translator.TestUtils#provideInvalidDigitalSpecimen")
  void testRetrieveDataInvalidSpecimen(DigitalSpecimen digitalSpecimen) throws Exception {
    // Given
    var expected = new TranslatorJobResult(JobState.FAILED, 0);
    givenDWCA("/dwca-rbins.zip");
    given(dwcaRepository.getCoreRecords(anyList(), anyString())).willReturn(givenSpecimenMap(9));
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(any(JsonNode.class), anyBoolean()))
        .willReturn(digitalSpecimen);

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expected);
    then(dwcaRepository).should(times(2)).createTable(anyString(), any(Term.class));
    then(dwcaRepository).should(times(2)).postRecords(anyString(), any(Term.class), anyList());
    then(sourceSystemComponent).should().storeEmlRecord(any(File.class));
    then(rabbitMqService).shouldHaveNoInteractions();
    cleanup("src/test/resources/dwca/test/dwca-rbins.zip");
  }


  private Map<String, ObjectNode> givenSpecimenMap(int amount) {
    var givenMap = new HashMap<String, ObjectNode>();
    for (int i = 0; i < amount; i++) {
      var objectNode = MAPPER.createObjectNode();
      objectNode.put("dwca:ID", "id" + 1);
      objectNode.put("dwc:basisOfRecord", "PreservedSpecimen");
      objectNode.set("extensions", MAPPER.createObjectNode());
      givenMap.put("id:" + i, objectNode);
    }
    return givenMap;
  }

  @Test
  void testRetrieveDataWithGbifMedia() throws Exception {
    // Given
    var expected = new TranslatorJobResult(JobState.COMPLETED, 19);
    givenDWCA("/dwca-kew-gbif-media.zip");
    given(dwcaRepository.getCoreRecords(anyList(), anyString())).willReturn(givenSpecimenMap(19));
    given(dwcaRepository.getRecords(anyList(),
        eq("temp_extension_gw0_tyl_yru_identification"))).willReturn(
        Map.of());
    given(dwcaRepository.getRecords(anyList(),
        eq("temp_extension_gw0_tyl_yru_multimedia"))).willReturn(
        givenImageMap(19));
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(any(JsonNode.class), anyBoolean()))
        .willReturn(givenDigitalSpecimen());
    given(digitalSpecimenDirector.assembleDigitalMedia(anyBoolean(), any(JsonNode.class),
        anyString())).willReturn(givenDigitalMedia());
    given(fdoProperties.getDigitalMediaType()).willReturn("Doi of the digital media object");
    given(fdoProperties.getDigitalSpecimenType()).willReturn("Doi of the digital specimen");

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expected);
    then(dwcaRepository).should(times(3)).createTable(anyString(), any(Term.class));
    then(dwcaRepository).should(times(2)).postRecords(anyString(), any(Term.class), anyList());
    then(sourceSystemComponent).should().storeEmlRecord(any(File.class));
    then(rabbitMqService).should(times(19)).sendMessage(any(
        DigitalSpecimenEvent.class));
    cleanup("src/test/resources/dwca/test/dwca-kew-gbif-media.zip");
  }

  private Map<String, List<ObjectNode>> givenImageMap(int amount) {
    var givenMap = new HashMap<String, List<ObjectNode>>();
    for (int i = 0; i < amount; i++) {
      var objectNode = MAPPER.createObjectNode();
      objectNode.put("dwca:ID", "id" + 1);
      objectNode.put("dwc:basisOfRecord", "PreservedSpecimen");
      givenMap.put("id:" + i, List.of(objectNode));
    }
    return givenMap;
  }

  @Test
  void testRetrieveDataWithAcMedia() throws Exception {
    // Given
    var expected = new TranslatorJobResult(JobState.COMPLETED, 14);
    givenDWCA("/dwca-naturalis-ac-media.zip");
    given(dwcaRepository.getCoreRecords(anyList(), anyString())).willReturn(givenSpecimenMap(14));
    given(dwcaRepository.getRecords(anyList(),
        eq("temp_extension_gw0_tyl_yru_multimedia"))).willReturn(givenImageMap(14));
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(any(JsonNode.class), anyBoolean()))
        .willReturn(givenDigitalSpecimen());
    given(digitalSpecimenDirector.assembleDigitalMedia(anyBoolean(), any(JsonNode.class),
        anyString())).willReturn(givenDigitalMedia());
    given(fdoProperties.getDigitalMediaType()).willReturn("Doi of the digital media object");
    given(fdoProperties.getDigitalSpecimenType()).willReturn("Doi of the digital specimen");

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expected);
    then(dwcaRepository).should(times(2)).createTable(anyString(), any(Term.class));
    then(dwcaRepository).should(times(2)).postRecords(anyString(), any(Term.class), anyList());
    then(sourceSystemComponent).should().storeEmlRecord(any(File.class));
    then(rabbitMqService).should(times(14)).sendMessage(any(
        DigitalSpecimenEvent.class));
    cleanup("src/test/resources/dwca/test/dwca-naturalis-ac-media.zip");
  }

  @Test
  void testRetrieveDataWithInvalidAcMedia() throws Exception {
    // Given
    var expected = new TranslatorJobResult(JobState.COMPLETED, 1);
    givenDWCA("/dwca-invalid-ac-media.zip");
    given(dwcaRepository.getCoreRecords(anyList(), anyString())).willReturn(givenSpecimenMap(1));
    given(dwcaRepository.getRecords(anyList(),
        eq("temp_extension_gw0_tyl_yru_multimedia"))).willReturn(givenImageMap(1));
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(any(JsonNode.class), anyBoolean()))
        .willReturn(givenDigitalSpecimen());
    given(digitalSpecimenDirector.assembleDigitalMedia(anyBoolean(), any(JsonNode.class),
        anyString())).willReturn(new DigitalMedia());
    given(fdoProperties.getDigitalSpecimenType()).willReturn("Doi of the digital specimen");

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expected);
    var captor = ArgumentCaptor.forClass(DigitalSpecimenEvent.class);
    then(dwcaRepository).should(times(2)).createTable(anyString(), any(Term.class));
    then(dwcaRepository).should(times(2)).postRecords(anyString(), any(Term.class), anyList());
    then(sourceSystemComponent).should().storeEmlRecord(any(File.class));
    then(rabbitMqService).should(times(1)).sendMessage(captor.capture());
    assertThat(captor.getValue().digitalMediaEvents()).isEmpty();
    cleanup("src/test/resources/dwca/test/dwca-invalid-ac-media.zip");
  }

  @Test
  void testRetrieveOnlyOccurrence() throws Exception {
    // Given
    var expected = new TranslatorJobResult(JobState.FAILED, 0);
    givenDWCA("/dwca-only-occurrences.zip");

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expected);
    then(dwcaRepository).should(times(2)).createTable(anyString(), any(Term.class));
    then(dwcaRepository).should(times(0)).postRecords(anyString(), any(Term.class), anyList());
    then(sourceSystemComponent).should().storeEmlRecord(any(File.class));
    then(rabbitMqService).shouldHaveNoInteractions();
    cleanup("src/test/resources/dwca/test/dwca-only-occurrences.zip");
  }

  @Test
  void testRetrieveDataWithAssociatedMedia() throws Exception {
    // Given
    var expected = new TranslatorJobResult(JobState.COMPLETED, 20);
    givenDWCA("/dwca-lux-associated-media.zip");
    given(dwcaRepository.getCoreRecords(anyList(), anyString())).willReturn(
        givenSpecimenMapWithMedia(20));
    given(digitalSpecimenDirector.assembleDigitalSpecimenTerm(any(JsonNode.class), anyBoolean()))
        .willReturn(TestUtils.givenDigitalSpecimen());
    given(digitalSpecimenDirector.assembleDigitalMedia(anyBoolean(), any(JsonNode.class),
        anyString())).willReturn(givenDigitalMedia());
    given(fdoProperties.getDigitalMediaType()).willReturn("Doi of the digital media object");
    given(fdoProperties.getDigitalSpecimenType()).willReturn("Doi of the digital specimen");

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expected);
    then(dwcaRepository).should(times(1)).createTable(anyString(), any(Term.class));
    then(dwcaRepository).should(times(1)).postRecords(anyString(), any(Term.class), anyList());
    then(sourceSystemComponent).should().storeEmlRecord(any(File.class));
    then(rabbitMqService).should(times(20)).sendMessage(any(
        DigitalSpecimenEvent.class));
    cleanup("src/test/resources/dwca/test/dwca-lux-associated-media.zip");
  }


  private Map<String, ObjectNode> givenSpecimenMapWithMedia(int amount) {
    var givenMap = new HashMap<String, ObjectNode>();
    for (int i = 0; i < amount; i++) {
      var objectNode = MAPPER.createObjectNode();
      objectNode.put("dwca:ID", "id" + 1);
      objectNode.put("dwc:basisOfRecord", "PreservedSpecimen");
      objectNode.put("dwc:associatedMedia", "https://test.test | https://test2.test");
      givenMap.put("id:" + i, objectNode);
    }
    return givenMap;
  }

  private void givenSourceSystem() {
    given(sourceSystemComponent.getSourceSystemEndpoint()).willReturn("https://endpoint.com");
    given(sourceSystemComponent.getSourceSystemID()).willReturn(SOURCE_SYSTEM_ID);
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

  @Test
  void testRetrieveDataNull() throws Exception {
    // Given
    var expected = new TranslatorJobResult(JobState.FAILED, 0);
    givenDWCA("/dwca-lux-associated-media.zip");
    var nullMap = new HashMap<String, ObjectNode>();
    nullMap.put("id", null);
    given(dwcaRepository.getCoreRecords(anyList(), anyString())).willReturn(nullMap);

    // When
    var result = service.retrieveData();

    // Then
    assertThat(result).isEqualTo(expected);
    then(dwcaRepository).should(times(1)).createTable(anyString(), any(Term.class));
    then(dwcaRepository).should(times(1)).postRecords(anyString(), any(Term.class), anyList());
    then(sourceSystemComponent).should().storeEmlRecord(any(File.class));
    then(rabbitMqService).shouldHaveNoInteractions();
    cleanup("src/test/resources/dwca/test/dwca-lux-associated-media.zip");
  }

  private String getAbsolutePath() {
    String path = "src/test/resources/dwca/test";
    File file = new File(path);
    return file.getAbsolutePath();
  }

}
