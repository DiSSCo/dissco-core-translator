package eu.dissco.core.translator.domain;

public enum RelationshipType {
  HAS_ORGANISATION_ID("hasOrganisationID"),
  HAS_SOURCE_SYSTEM_ID("hasSourceSystemID"),
  HAS_FDO_TYPE("hasFDOType"),
  HAS_PHYSICAL_IDENTIFIER("hasPhysicalIdentifier"),
  HAS_LICENSE("hasLicense"),
  HAS_REFERENCE("hasReference"),
  HAS_URL("hasURL"),
  HAS_SOURCE("hasSource"),;

  private final String name;

  RelationshipType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

}
