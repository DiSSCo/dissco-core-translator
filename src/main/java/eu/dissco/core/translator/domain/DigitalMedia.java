package eu.dissco.core.translator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenID;

public record DigitalMedia(
    @JsonProperty("ods:type")
    String type,
    @JsonProperty(PhysicalSpecimenID.TERM)
    String physicalSpecimenId,
    @JsonProperty("ods:attributes")
    eu.dissco.core.translator.schema.DigitalMedia attributes,
    @JsonProperty("ods:originalAttributes")
    JsonNode originalAttributes) {

}
