package eu.dissco.core.translator.domain;

import java.util.List;

public record DigitalSpecimenEvent(
    List<String> enrichmentList,
    DigitalSpecimen digitalSpecimen,
    List<DigitalMediaObjectEvent> digitalMediaObjectEvents) {

}
