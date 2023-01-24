package eu.dissco.core.translator.domain;

import com.fasterxml.jackson.databind.JsonNode;

public record DigitalSpecimen(String id, String type, JsonNode attributes, JsonNode originalAttributes) {

}
