package eu.dissco.core.translator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.dissco.core.translator.schema.DigitalSpecimen;
import tools.jackson.databind.JsonNode;


public record DigitalSpecimenWrapper(
    @JsonProperty("ods:normalisedPhysicalSpecimenID")
    String id,
    @JsonProperty("ods:type")
    String type,
    @JsonProperty("ods:attributes")
    DigitalSpecimen attributes,
    @JsonProperty("ods:originalAttributes")
    JsonNode originalAttributes) {

}
