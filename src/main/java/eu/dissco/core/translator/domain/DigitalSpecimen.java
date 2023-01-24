package eu.dissco.core.translator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenId;
import eu.dissco.core.translator.terms.specimen.Type;


public record DigitalSpecimen(
    @JsonProperty(PhysicalSpecimenId.TERM)
    String id,
    @JsonProperty(Type.TERM)
    String type,
    @JsonProperty("ods:attributes")
    JsonNode attributes,
    @JsonProperty("ods:originalAttributes")
    JsonNode originalAttributes) {

}
