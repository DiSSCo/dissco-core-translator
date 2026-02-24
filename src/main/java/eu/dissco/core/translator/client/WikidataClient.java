package eu.dissco.core.translator.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface WikidataClient {

  @GetExchange("/{wikidataId}/labels/en")
  String getWikidataLabel(@PathVariable String wikidataId);
}
