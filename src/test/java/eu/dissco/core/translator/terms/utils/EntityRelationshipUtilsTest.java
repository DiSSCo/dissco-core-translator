package eu.dissco.core.translator.terms.utils;

import static eu.dissco.core.translator.domain.AgentRoleType.DATA_TRANSLATOR;
import static eu.dissco.core.translator.domain.RelationshipType.HAS_FDO_TYPE;
import static eu.dissco.core.translator.domain.RelationshipType.HAS_ORGANISATION_ID;
import static eu.dissco.core.translator.schema.Agent.Type.SCHEMA_SOFTWARE_APPLICATION;
import static eu.dissco.core.translator.schema.Identifier.OdsIdentifierStatus.PREFERRED;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.domain.RelationshipType;
import eu.dissco.core.translator.schema.EntityRelationship;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EntityRelationshipUtilsTest {

  public static final String APP_NAME = "DiSSCo Translator Service";
  public static final String APP_PID = "https://hdl.handle.net/TEST/XXX-XXX-XXX";

  public static Stream<Arguments> erProvider() throws URISyntaxException {
    return Stream.of(
        Arguments.of(HAS_ORGANISATION_ID, "https://ror.org/0566bfb96",
            new URI("https://ror.org/0566bfb96")
        ),
        Arguments.of(HAS_ORGANISATION_ID, "0566bfb96", null
        ),
        Arguments.of(HAS_ORGANISATION_ID, "https://ror.org/05^^@^#^66bfb96", null
        )
    );
  }

  @ParameterizedTest
  @MethodSource("erProvider")
  void testAddEntityRelationship(RelationshipType relationshipType, String relatedResource,
      URI relatedResourceURI) {
    // Given
    var expected = createEntityRelationship(relationshipType, relatedResource, relatedResourceURI);

    // When
    var result = EntityRelationshipUtils.addEntityRelationship(relationshipType, relatedResource,
        APP_NAME, APP_PID);

    // Then
    assertThat(result).usingRecursiveComparison().ignoringFields("dwcRelationshipEstablishedDate")
        .isEqualTo(expected);
  }

  @Test
  void testNullRelatedResource() {
    // When
    var result = EntityRelationshipUtils.addEntityRelationship(HAS_FDO_TYPE, null, APP_NAME,
        APP_PID);

    // Then
    assertThat(result).isNull();
  }

  private EntityRelationship createEntityRelationship(RelationshipType relationshipType,
      String relatedResource, URI relatedResourceURI) {
    return new EntityRelationship()
        .withType("ods:EntityRelationship")
        .withDwcRelationshipOfResource(relationshipType.getName())
        .withDwcRelatedResourceID(relatedResource)
        .withOdsRelatedResourceURI(relatedResourceURI)
        .withDwcRelationshipEstablishedDate(Date.from(Instant.now()))
        .withOdsHasAgents(AgentsUtils.addAgent(List.of(), APP_NAME, APP_PID,
            DATA_TRANSLATOR, SCHEMA_SOFTWARE_APPLICATION, PREFERRED));

  }
}
