package eu.dissco.core.translator.domain;

import com.fasterxml.jackson.databind.JsonNode;

public record DigitalMediaObject(
    String type,
    String physicalSpecimenId,
    String idType,
    JsonNode attributes,
    JsonNode originalAttributes) {

}
