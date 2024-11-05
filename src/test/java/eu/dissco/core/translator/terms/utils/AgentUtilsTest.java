package eu.dissco.core.translator.terms.utils;

import static eu.dissco.core.translator.domain.AgentRoleType.CREATOR;
import static eu.dissco.core.translator.schema.Agent.Type.SCHEMA_PERSON;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.schema.Agent;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AgentUtilsTest {

  public static Stream<Arguments> agentProvider() {
    return Stream.of(
        Arguments.of(null, null, List.of()
        ),
        Arguments.of("Fricke, Ronald", "http://orcid.org/0000-0002-9079-593X",
            List.of(createAgent("Fricke, Ronald", "http://orcid.org/0000-0002-9079-593X"))
        ),
        Arguments.of("Fricke, Ronald", null,
            List.of(createAgent("Fricke, Ronald", null))
        ),
        Arguments.of("http://orcid.org/0000-0002-9079-593X", null,
            List.of(createAgent(null, "http://orcid.org/0000-0002-9079-593X"))
        ),
        Arguments.of("http://orcid.org/0000-0002-9079-593X",
            "https://orcid.org/0000-0001-9790-9277",
            List.of(createAgent("http://orcid.org/0000-0002-9079-593X",
                "https://orcid.org/0000-0001-9790-9277"))
        ),
        Arguments.of("  Fricke, Ronald  | Tom Dijkema  ",
            "http://orcid.org/0000-0002-9079-593X |  https://orcid.org/0000-0001-9790-9277  ",
            List.of(
                createAgent("Fricke, Ronald", "http://orcid.org/0000-0002-9079-593X"),
                createAgent("Tom Dijkema", "https://orcid.org/0000-0001-9790-9277"))
        ),
        Arguments.of("  Fricke, Ronald  & https://orcid.org/0000-0001-9790-9277  ", null,
            List.of(
                createAgent("Fricke, Ronald", null),
                createAgent(null, "https://orcid.org/0000-0001-9790-9277"))
        ),
        Arguments.of("Fricke, Ronald",
            "http://orcid.org/0000-0002-9079-593X |  https://orcid.org/0000-0001-9790-9277",
            List.of(
                createAgent(null, "http://orcid.org/0000-0002-9079-593X"),
                createAgent(null, "https://orcid.org/0000-0001-9790-9277"))
        ),
        Arguments.of("Fricke, Ronald & Tom Dijkema",
            "https://orcid.org/0000-0001-9790-9277",
            List.of(
                createAgent("Fricke, Ronald", null),
                createAgent("Tom Dijkema", null))
        )
    );
  }

  private static Agent createAgent(String name, String id) {
    var agent = new Agent()
        .withId(id)
        .withType(SCHEMA_PERSON)
        .withSchemaName(name)
        .withSchemaIdentifier(id)
        .withOdsHasRoles(
            List.of(new eu.dissco.core.translator.schema.OdsHasRole().withType("schema:Role")
                .withSchemaRoleName(CREATOR.getName())));
    if (id != null) {
      agent.withOdsHasIdentifiers(
          List.of(new eu.dissco.core.translator.schema.Identifier().withType("ods:Identifier")
              .withId(id).withDctermsIdentifier(id)));
    }
    return agent;
  }

  @ParameterizedTest
  @MethodSource("agentProvider")
  void testSetAgent(String termValue, String id, List<Agent> expected) {

    // When
    var result = AgentsUtils.setAgent(null, termValue, id, CREATOR, SCHEMA_PERSON);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testSetAgentUnmutableLiat() {
    // Given
    var agentOne = createAgent("Tom Dijkema", "https://orcid.org/0000-0001-9790-9277");
    var agentTwo = createAgent("Fricke, Ronald", "http://orcid.org/0000-0002-9079-593X");

    // When
    var result = AgentsUtils.setAgent(
        List.of(agentOne),
        "Fricke, Ronald", "http://orcid.org/0000-0002-9079-593X", CREATOR, SCHEMA_PERSON);

    // Then
    assertThat(result).isEqualTo(List.of(agentOne, agentTwo));
  }

}
