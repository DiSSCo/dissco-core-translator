package eu.dissco.core.translator.domain;

import lombok.Getter;

@Getter
public enum AgentRoleType {

  COLLECTOR("collector"),
  DATA_TRANSLATOR("data-translator"),
  CREATOR("creator"),
  IDENTIFIER("identifier"),
  GEOREFERENCER("georeferencer"),
  RIGHTS_OWNER("rights-owner");

  private final String name;

  AgentRoleType(String name) {
    this.name = name;
  }

}