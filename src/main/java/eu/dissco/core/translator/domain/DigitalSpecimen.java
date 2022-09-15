package eu.dissco.core.translator.domain;

import com.fasterxml.jackson.databind.JsonNode;

public record DigitalSpecimen(
    String type,
    String physicalSpecimenId,
    String physicalSpecimenIdType,
    String physicalSpecimenCollection,
    String specimenName,
    String organizationId,
    String datasetId,
    String sourceSystemId,
    JsonNode data,
    JsonNode originalData,
    String dwcaId) {

}
