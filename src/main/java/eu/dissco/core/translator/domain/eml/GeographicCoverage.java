package eu.dissco.core.translator.domain.eml;

public record GeographicCoverage(
    String geographicDescription,
    BoundingCoordinates boundingCoordinates
) {

}
