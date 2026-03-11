package eu.dissco.core.translator.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import tools.jackson.databind.JsonNode;

public interface RorClient {

    @GetExchange("/{ror}")
    JsonNode getRorInformation(@PathVariable String ror);
}
