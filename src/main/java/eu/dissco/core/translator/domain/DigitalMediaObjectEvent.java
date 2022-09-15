package eu.dissco.core.translator.domain;

import java.util.List;

public record DigitalMediaObjectEvent(
    List<String> enrichmentList,
    DigitalMediaObject digitalMediaObject) {

}
