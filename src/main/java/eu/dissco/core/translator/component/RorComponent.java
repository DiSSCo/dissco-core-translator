package eu.dissco.core.translator.component;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class RorComponent {

  private final WebClient webClient;

  @Cacheable("ror")
  public String getRoRId(String ror) {
    log.info("Requesting organisation details for organisation: {} with ror", ror);
    String url = "https://api.ror.org/organizations/" + ror;
    var uriSpec = webClient.get().uri(url).retrieve();
    var response = uriSpec.bodyToMono(JsonNode.class);
    try {
      var json = response.toFuture().get();
      if (json != null) {
        var name = json.get("name");
        if (name != null) {
          return name.asText();
        } else {
          return null;
        }
      }
    } catch (InterruptedException | ExecutionException e) {
      log.error("Failed to make request to RoR service", e);
      Thread.currentThread().interrupt();
    }
    log.warn("Could not match name to a ROR id for: {}", url);
    return null;
  }
}
