package eu.dissco.core.translator.component;

import eu.dissco.core.translator.client.RorClient;
import eu.dissco.core.translator.client.WikidataClient;
import eu.dissco.core.translator.exception.OrganisationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrganisationNameComponent {

  private final RorClient rorClient;
  private final WikidataClient wikidataClient;
  private static final String NAMES = "names";
  private static final String ORG_NAME_ERROR = "Unable to retrieve organisationName";

  @Cacheable("ror")
  public String getRorName(String ror) throws OrganisationException {
    log.info("Requesting organisation details for organisation: {} with ror", ror);
    var response = rorClient.getRorInformation(ror);
    if (response != null) {
        return getRorInstitutionName(response);
    }
    log.warn("Could not match name to a ROR id for: {}", ror);
    throw new OrganisationException(ORG_NAME_ERROR);
  }

  private static String getRorInstitutionName(JsonNode rorResult) throws OrganisationException {
    try {
      if (rorResult.get(NAMES).isArray() && !rorResult.get(NAMES).isEmpty()) {
        for (var name : rorResult.get(NAMES)) {
          for (var type : name.get("types")) {
            if ("ror_display".equals(type.asString())) {
              return name.get("value").asString();
            }
          }
        }
        log.warn("No ror display names provided in ROR record. Using first name");
        return rorResult.get(NAMES).get(0).get("value").asString();
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
    var response = wikidataClient.getWikidataLabel(wikidata);
    if (response != null && response.isString()) {
      return response.asString().replace("\"", "");
    }
    log.warn("Could not match to an English (en) label to a wikidata id for: {}", wikidata);
    throw new OrganisationException(ORG_NAME_ERROR);
  }
}
