package eu.dissco.core.translator.terms.utils;

import eu.dissco.core.translator.domain.AgenRoleType;
import eu.dissco.core.translator.schema.Agent.Type;
import eu.dissco.core.translator.schema.Agent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentsUtils {

  public static void setAgent(List<Agent> currentAgents, String termValue, String id,
      AgenRoleType role, eu.dissco.core.translator.schema.Agent.Type type) {
    if (currentAgents == null){
      currentAgents = new ArrayList<>();
    }
    if (termValue != null || id != null) {
      if ((termValue != null && (termValue.contains("&") || termValue.contains("|"))) || (
          id != null && (id.contains("&") || id.contains("|")))) {
        handleMultipleAgents(currentAgents, termValue, id, role, type);
      } else {
        String name = termValue;
        if (termValue != null && termValue.contains("http") && id == null) {
          id = termValue;
          name = null;
        } else if (termValue != null && termValue.contains("http") && id != null
            && !id.equals(termValue)) {
          log.warn(
              "The id and term value do not match for term: {} and id: {}. Adding value as name and id as identifier",
              termValue, id);
        }
        var agent = new Agent()
            .withId(id)
            .withType(type)
            .withSchemaName(name)
            .withSchemaIdentifier(id)
            .withOdsHasRoles(
                List.of(new eu.dissco.core.translator.schema.OdsHasRole().withType("schema:Role")
                    .withSchemaRoleName(role.getName())));
        if (id != null) {
          agent.withOdsHasIdentifiers(List.of(
              new eu.dissco.core.translator.schema.Identifier().withId(id)
                  .withType("ods:Identifier")
                  .withDctermsIdentifier(id)));
        }
        currentAgents.add(agent);
      }
    }
  }

  private static void handleMultipleAgents(
      List<Agent> currentAgents, String termValue, String id, AgenRoleType role,
      eu.dissco.core.translator.schema.Agent.Type type) {
    var ids = new String[0];
    var agents = new String[0];
    if (termValue != null && (termValue.contains("&") || termValue.contains("|"))) {
      agents = Arrays.stream(termValue.split("[&|]")).map(String::trim).toArray(String[]::new);
    }
    if (id != null && (id.contains("&") || id.contains("|"))) {
      ids = Arrays.stream(id.split("[&|]")).map(String::trim).toArray(String[]::new);
    }
    if (agents.length == ids.length) {
      for (int i = 0; i < agents.length; i++) {
        setAgent(currentAgents, agents[i], ids[i], role, type);
      }
    } else if (agents.length > ids.length) {
      log.warn(
          "The number of agents values is greater than ids, ignoring ids for term: {} and id: {}",
          termValue, id);
      for (String agent : agents) {
        setAgent(currentAgents, agent, null, role, type);
      }
    } else {
      log.warn(
          "The number of ids is greater than agent values, ignoring agent values for term: {} and id: {}",
          termValue, id);
      for (String idValue : ids) {
        setAgent(currentAgents, null, idValue, role, type);
      }
    }
  }
}
