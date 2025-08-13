package eu.dissco.core.translator.domain;

import java.util.List;
import java.util.Set;

public record DigitalSpecimenEvent(
    Set<String> masList,
    DigitalSpecimenWrapper digitalSpecimenWrapper,
    List<DigitalMediaEvent> digitalMediaEvents,
    Boolean forceMasSchedule) {

}
