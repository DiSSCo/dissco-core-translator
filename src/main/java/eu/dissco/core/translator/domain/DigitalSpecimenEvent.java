package eu.dissco.core.translator.domain;

import java.util.List;

public record DigitalSpecimenEvent(
    List<String> enrichmentList,
    DigitalSpecimenWrapper digitalSpecimenWrapper,
    List<DigitalMediaObjectEvent> digitalMediaObjectEvents) {

}
