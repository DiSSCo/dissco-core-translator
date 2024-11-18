package eu.dissco.core.translator.terms.utils;

import eu.dissco.core.translator.domain.AgentRoleType;
import eu.dissco.core.translator.schema.Agent;
import eu.dissco.core.translator.schema.Agent.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentsUtils {

  private AgentsUtils() {
    // This is a Utility class
  }

  public static List<Agent> addAgent(List<Agent> currentAgents, String agentValue, String agentId,
      AgentRoleType role, Type type) {
    var agents = new ArrayList<Agent>();
    if (currentAgents != null) {
      agents.addAll(currentAgents);
    }
    if (agentValue != null || agentId != null) {
      if ((agentValue != null && (agentValue.contains("&") || agentValue.contains("|"))) || (
          agentId != null && (agentId.contains("&") || agentId.contains("|")))) {
        handleMultipleAgents(agents, agentValue, agentId, role, type);
      } else {
        constructAgent(agents, agentValue, agentId, role, type);
      }
    }
    return agents;
  }

  private static void constructAgent(List<Agent> agents, String agentValue, String agentId,
      AgentRoleType role, Type type) {
    String agentName = agentValue;
    if (agentValue != null && agentValue.contains("http") && agentId == null) {
      agentId = agentValue;
      agentName = null;
    } else if (agentValue != null && agentValue.contains("http") && agentId != null
        && !agentId.equals(agentValue)) {
      log.warn(
          "The agentId and agentValue do not match for term: {} and agentId: {}. Adding value as agentName and agentId as identifier",
          agentValue, agentId);
    }
    var agent = new Agent()
        .withId(agentId)
        .withType(type)
        .withSchemaName(agentName)
        .withSchemaIdentifier(agentId)
        .withOdsHasRoles(
            List.of(new eu.dissco.core.translator.schema.OdsHasRole().withType("schema:Role")
                .withSchemaRoleName(role.getName())));
    if (agentId != null) {
      agent.withOdsHasIdentifiers(List.of(
          new eu.dissco.core.translator.schema.Identifier().withId(agentId)
              .withType("ods:Identifier")
              .withDctermsIdentifier(agentId)));
    }
    agents.add(agent);
  }

  private static void handleMultipleAgents(
      List<Agent> agents, String agentValue, String agentId, AgentRoleType role, Type type) {
    var ids = new String[0];
    var agentValues = new String[0];
    if (checkIfNeedsParsing(agentValue)) {
      agentValues = Arrays.stream(agentValue.split("[&|]")).map(String::trim)
          .toArray(String[]::new);
    }
    if (checkIfNeedsParsing(agentId)) {
      ids = Arrays.stream(agentId.split("[&|]")).map(String::trim).toArray(String[]::new);
    }
    if (agentValues.length == ids.length) {
      for (int i = 0; i < agentValues.length; i++) {
        constructAgent(agents, agentValues[i], ids[i], role, type);
      }
    } else if (agentValues.length > ids.length) {
      if (ids.length != 0) {
        log.warn(
            "The number of agentValues values is greater than ids, ignoring ids for term: {} and agentId: {}",
            agentValue, agentId);
      }
      for (String agent : agentValues) {
        constructAgent(agents, agent, null, role, type);
      }
    } else {
      if (agentValues.length != 0) {
        log.warn(
            "The number of ids is greater than agentValue, ignoring agentValue values for term: {} and agentId: {}",
            agentValue, agentId);
      }
      for (String idValue : ids) {
        constructAgent(agents, null, idValue, role, type);
      }
    }
  }

  private static boolean checkIfNeedsParsing(String value) {
    return value != null && (value.contains("&") || value.contains("|"));
  }
}
