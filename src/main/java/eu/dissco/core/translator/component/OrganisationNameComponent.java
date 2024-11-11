package eu.dissco.core.translator.component;

import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.exception.OrganisationException;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrganisationNameComponent {

  private final WebClient webClient;
  private static final String NAMES = "names";
  private static final String ORG_NAME_ERROR = "Unable to retrieve organisationName";

  @Cacheable("ror")
  public String getRorName(String ror) throws OrganisationException {
    log.info("Requesting organisation details for organisation: {} with ror", ror);
    String url = "https://api.ror.org/v2/organizations/" + ror;
    var response = webClient.get().uri(url).retrieve().bodyToMono(JsonNode.class)
        .publishOn(Schedulers.boundedElastic());
    try {
      var json = response.toFuture().get();
      if (json != null) {
        return getRorInstitutionName(json);
      }
    } catch (InterruptedException e) {
      log.error("Failed to make request to RoR service", e);
      Thread.currentThread().interrupt();
    } catch (ExecutionException e) {
      log.error("Failed to make request to RoR service", e);
    }
    log.warn("Could not match name to a ROR id for: {}", url);
    throw new OrganisationException(ORG_NAME_ERROR);
  }

  private static String getRorInstitutionName(JsonNode rorResult) throws OrganisationException {
    try {
      if (rorResult.get(NAMES).isArray() && !rorResult.get(NAMES).isEmpty()) {
        for (var name : rorResult.get(NAMES)) {
          for (var type : name.get("types")) {
            if ("ror_display".equals(type.asText())) {
              return name.get("value").asText();
            }
          }
        }
        log.warn("No ror display names provided in ROR record. Using first name");
        return rorResult.get(NAMES).get(0).get("value").asText();
      }
      log.error("Unable to parse ROR result {}", rorResult);
      throw new OrganisationException(ORG_NAME_ERROR);
    } catch (NullPointerException e) {
      log.error("Unexpected ROR result {}", rorResult, e);
      throw new OrganisationException(ORG_NAME_ERROR);
    }
  }

  @Cacheable("wikidata")
  public String getWikiDataName(String wikidata) throws OrganisationException {
    log.info("Requesting organisation details for organisation: {} with wikidata", wikidata);
    String url = "https://www.wikidata.org/w/rest.php/wikibase/v0/entities/items/" + wikidata + "/labels/en";
    var response = webClient.get().uri(url).retrieve().bodyToMono(JsonNode.class)
        .publishOn(Schedulers.boundedElastic());
    try {
      var name = response.toFuture().get();
      if (name != null && name.isTextual()) {
        return name.asText();
      }
    } catch (InterruptedException e) {
      log.error("Failed to make request to wikidata service", e);
      Thread.currentThread().interrupt();
    } catch (ExecutionException e) {
      log.error("Failed to make request to wikidata service", e);
    }
    log.warn("Could not match to an English (en) label to a wikidata id for: {}", url);
    throw new OrganisationException(ORG_NAME_ERROR);
  }
}
