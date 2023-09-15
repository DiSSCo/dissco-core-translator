package eu.dissco.core.translator.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenId;
import eu.dissco.core.translator.terms.specimen.Type;
import java.util.List;


public record DigitalSpecimen(
    @JsonProperty(PhysicalSpecimenId.TERM)
    String id,
    @JsonProperty(Type.TERM)
    String type,
    @JsonProperty("ods:attributes")
    eu.dissco.core.translator.schema.DigitalSpecimen attributes,
    @JsonProperty("ods:originalAttributes")
    JsonNode originalAttributes,

    @JsonProperty("ods:digitalMediaoBjects")
    List<DigitalMediaObject> digitalMediaObjects) {

}
