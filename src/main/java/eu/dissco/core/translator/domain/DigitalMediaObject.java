package eu.dissco.core.translator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.media.MediaType;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenId;

public record DigitalMediaObject(
    @JsonProperty(MediaType.TERM)
    String type,
    @JsonProperty(PhysicalSpecimenId.TERM)
    String physicalSpecimenId,
    @JsonProperty("ods:mediaIdType")
    String idType,
    @JsonProperty("ods:attributes")
    JsonNode attributes,
    @JsonProperty("ods:originalAttributes")
    JsonNode originalAttributes) {

}
