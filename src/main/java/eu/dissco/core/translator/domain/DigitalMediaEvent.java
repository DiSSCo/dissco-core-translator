package eu.dissco.core.translator.domain;

import java.util.Set;

public record DigitalMediaEvent(
    Set<String> masList,
    DigitalMediaWrapper digitalMedia, Boolean forceMasSchedule) {

}
