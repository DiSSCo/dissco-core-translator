package eu.dissco.core.translator.domain;

import java.util.List;

public record DigitalMediaEvent(
    List<String> enrichmentList,
    DigitalMedia digitalMedia) {

}
