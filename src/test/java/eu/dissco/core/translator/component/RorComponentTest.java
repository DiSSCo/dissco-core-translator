package eu.dissco.core.translator.component;

import static eu.dissco.core.translator.TestUtils.loadResourceFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class RorComponentTest {

  private static final String ROR = "03srysw20";
  private static final String ORGANISATION_NAME = "The MuseumFactory";
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
  private RorComponent rorComponent;

  @BeforeEach
  void setup() {
    this.rorComponent = new RorComponent(client);
  }


  @Test
  void testGetRorId() throws ExecutionException, InterruptedException, IOException {
    // Given
    givenWebclient();
    given(jsonFuture.get()).willReturn(
        mapper.readTree(loadResourceFile("ror/example-ror.json")));

    // When
    var result = rorComponent.getRoRId(ROR);

    // Then
    assertThat(result).isEqualTo(ORGANISATION_NAME);
  }

  @Test
  void testResponseInvalid() throws ExecutionException, InterruptedException, IOException {
    // Given
    givenWebclient();
    given(jsonFuture.get()).willReturn(
        mapper.readTree(loadResourceFile("ror/response-invalid.json")));

    // When
    var result = rorComponent.getRoRId(ROR);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testEmptyMono() throws ExecutionException, InterruptedException {
    // Given
    givenWebclient();
    given(jsonFuture.get()).willReturn(null);

    // When
    var result = rorComponent.getRoRId(ROR);

    // Then
    assertThat(result).isNull();
  }

  private void givenWebclient() {
    given(client.get()).willReturn(headersSpec);
    given(headersSpec.uri(anyString())).willReturn(uriSpec);
    given(uriSpec.retrieve()).willReturn(responseSpec);
    given(responseSpec.bodyToMono(any(Class.class))).willReturn(jsonNodeMono);
    given(jsonNodeMono.toFuture()).willReturn(jsonFuture);
  }
}
