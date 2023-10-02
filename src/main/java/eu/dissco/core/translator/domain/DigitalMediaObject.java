package eu.dissco.core.translator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.schema.DigitalEntity;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenId;

public record DigitalMediaObject(
    @JsonProperty("ods:type")
    String type,
    @JsonProperty(PhysicalSpecimenId.TERM)
    String physicalSpecimenId,
    @JsonProperty("ods:attributes")
    DigitalEntity attributes,
    @JsonProperty("ods:originalAttributes")
    JsonNode originalAttributes) {

}
