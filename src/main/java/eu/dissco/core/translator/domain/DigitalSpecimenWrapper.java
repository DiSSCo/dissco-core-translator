package eu.dissco.core.translator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenId;
import eu.dissco.core.translator.schema.DigitalSpecimen;


public record DigitalSpecimenWrapper(
    @JsonProperty("ods:normalisedPhysicalSpecimenId")
    String id,
    @JsonProperty("ods:type")
    String type,
    @JsonProperty("ods:attributes")
    DigitalSpecimen attributes,
    @JsonProperty("ods:originalAttributes")
    JsonNode originalAttributes) {

}
