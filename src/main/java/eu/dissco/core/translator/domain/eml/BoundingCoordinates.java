package eu.dissco.core.translator.domain.eml;

public record BoundingCoordinates(
    String westBoundingCoordinate,
    String eastBoundingCoordinate,
    String northBoundingCoordinate,
    String southBoundingCoordinate
) {

}
