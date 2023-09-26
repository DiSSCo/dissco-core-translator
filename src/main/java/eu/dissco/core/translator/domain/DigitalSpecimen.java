package eu.dissco.core.translator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenId;


public record DigitalSpecimen(
    @JsonProperty(PhysicalSpecimenId.TERM)
    String id,
    @JsonProperty("ods:type")
    String type,
    @JsonProperty("ods:attributes")
    eu.dissco.core.translator.schema.DigitalSpecimen attributes,
    @JsonProperty("ods:originalAttributes")
    JsonNode originalAttributes) {

}
