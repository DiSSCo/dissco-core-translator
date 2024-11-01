package eu.dissco.core.translator.domain;

public enum AgenRoleType {

  COLLECTOR("collector"),
  DATA_TRANSLATOR("data-translator"),
  CREATOR("creator"),
  IDENTIFIER("identifier"),
  GEOREFERENCER("georeferencer"),
  RIGHTS_OWNER("rights-owner");

  private final String name;

  AgenRoleType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
