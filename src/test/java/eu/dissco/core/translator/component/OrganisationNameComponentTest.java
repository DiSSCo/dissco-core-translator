package eu.dissco.core.translator.component;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.loadResourceFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.exception.OrganisationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@ExtendWith(MockitoExtension.class)
class OrganisationNameComponentTest {

  private static final String ROR = "03srysw20";
  private static final String WIKIDATA_ID = "Q2203052";
  private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
  @Mock
  private WebClient client;
  @Mock
  private RequestHeadersUriSpec headersSpec;
  @Mock
  private RequestHeadersSpec uriSpec;
  @Mock
  private ResponseSpec responseSpec;
  @Mock
  private Mono<JsonNode> jsonNodeMono;
  @Mock
  private CompletableFuture<JsonNode> jsonFuture;
  private OrganisationNameComponent rorComponent;

  @BeforeEach
  void setup() {
    this.rorComponent = new OrganisationNameComponent(client);
  }


  @Test
  void testGetRorId() throws Exception {
    // Given
    givenWebclient();
    given(jsonFuture.get()).willReturn(
        mapper.readTree(loadResourceFile("organisation-name/example-ror.json")));

    // When
    var result = rorComponent.getRorName(ROR);

    // Then
    assertThat(result).isEqualTo("The MuseumFactory");
  }

  @Test
  void testGetRorIdFirst() throws Exception {
    // Given
    givenWebclient();
    given(jsonFuture.get()).willReturn(
        mapper.readTree("""
            {
              "names": [
                {
                  "lang": "nl",
                  "types": [
                    "label"
                    ],
                  "value": "De MuseumFabriek"
                  }
              ]
            }
            """));

    // When
    var result = rorComponent.getRorName(ROR);

    // Then
    assertThat(result).isEqualTo("De MuseumFabriek");
  }

  @ParameterizedTest
  @MethodSource("badRorResponse")
  void testBadRorResponse(JsonNode rorResponse) throws Exception  {
    // Given
    givenWebclient();
    given(jsonFuture.get()).willReturn(rorResponse);

    // When / Then
    assertThrows(OrganisationException.class, () -> rorComponent.getRorName(ROR));

  }

  private static Stream<Arguments> badRorResponse() throws JsonProcessingException {
    return Stream.of(
        Arguments.of(
            MAPPER.readTree("""
                {
                  "names":"De MuseumFabriek"
                }
                """)
        ),
        Arguments.of(
            MAPPER.readTree("""
                {
                  "names": ["De MuseumFabriek"]
                }
                """)
        )
    );
  }

  @Test
  void testWikidataId() throws Exception {
    // Given
    givenWebclient();
    given(jsonFuture.get()).willReturn(
        mapper.readTree(loadResourceFile("organisation-name/example-wikidata.json")));

    // When
    var result = rorComponent.getWikiDataName(WIKIDATA_ID);

    // Then
    assertThat(result).isEqualTo("De Museumfabriek");
  }

  @Test
  void testWikidataIdInvalid() throws Exception {
    // Given
    givenWebclient();
    given(jsonFuture.get()).willReturn(
        mapper.readTree(loadResourceFile("organisation-name/invalid-wikidata.json")));

    // When / Then
    assertThrows(OrganisationException.class, () -> rorComponent.getWikiDataName(ROR));
  }


  @Test
  void testResponseInvalid() throws Exception {
    // Given
    givenWebclient();
    given(jsonFuture.get()).willReturn(
        mapper.readTree(loadResourceFile("organisation-name/response-invalid.json")));

    // When / Then
    assertThrows(OrganisationException.class, () -> rorComponent.getRorName(ROR));
  }

  @Test
  void testWebClientIssueRor() throws Exception {
    // Given
    givenWebclient();
    given(jsonFuture.get()).willThrow(new ExecutionException(new RuntimeException()));

    // When / Then
    assertThrows(OrganisationException.class, () -> rorComponent.getRorName(ROR));
  }

  @Test
  void testWebClientIssueWikidata() throws Exception {
    // Given
    givenWebclient();
    given(jsonFuture.get()).willThrow(new ExecutionException(new RuntimeException()));

    // When / Then
    assertThrows(OrganisationException.class, () -> rorComponent.getWikiDataName(WIKIDATA_ID));
  }

  @Test
  void testEmptyMono() throws Exception {
    // Given
    givenWebclient();
    given(jsonFuture.get()).willReturn(null);

    // When / Then
    assertThrows(OrganisationException.class, () -> rorComponent.getRorName(ROR));
  }

  private void givenWebclient() {
    given(client.get()).willReturn(headersSpec);
    given(headersSpec.uri(anyString())).willReturn(uriSpec);
    given(uriSpec.retrieve()).willReturn(responseSpec);
    given(responseSpec.bodyToMono(any(Class.class))).willReturn(jsonNodeMono);
    given(jsonNodeMono.publishOn(any(Scheduler.class))).willReturn(jsonNodeMono);
    given(jsonNodeMono.toFuture()).willReturn(jsonFuture);
  }
}
