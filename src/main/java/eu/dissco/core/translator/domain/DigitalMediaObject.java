package eu.dissco.core.translator.domain;

import com.fasterxml.jackson.databind.JsonNode;

public record DigitalMediaObject(
    String type,
    String physicalSpecimenId,
    String mediaUrl,
    String format,
    String sourceSystemId,
    JsonNode data,
    JsonNode originalData,
    String idType) {

}
