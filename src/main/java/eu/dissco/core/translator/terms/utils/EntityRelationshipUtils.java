package eu.dissco.core.translator.terms.utils;

import static eu.dissco.core.translator.domain.AgentRoleType.DATA_TRANSLATOR;
import static eu.dissco.core.translator.schema.Agent.Type.SCHEMA_SOFTWARE_APPLICATION;
import static eu.dissco.core.translator.schema.Identifier.OdsIdentifierStatus.PREFERRED;
import static eu.dissco.core.translator.terms.utils.AgentsUtils.addAgent;

import eu.dissco.core.translator.domain.RelationshipType;
import eu.dissco.core.translator.schema.EntityRelationship;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityRelationshipUtils {

  private EntityRelationshipUtils() {
    // This is a Utility class
  }

  public static EntityRelationship addEntityRelationship(
      RelationshipType relationshipType, String relatedResource, String agentName, String agentId) {
    if (relatedResource == null) {
      log.warn("Related resource for type {} is null. Skipping entity relationship creation.",
          relationshipType.getName());
      return null;
    }
    var entityRelationship = new EntityRelationship()
        .withType("ods:EntityRelationship")
        .withDwcRelationshipOfResource(relationshipType.getName())
        .withDwcRelatedResourceID(relatedResource)
        .withDwcRelationshipEstablishedDate(Date.from(Instant.now()));
    entityRelationship.setOdsHasAgents(
        addAgent(entityRelationship.getOdsHasAgents(), agentName, agentId, DATA_TRANSLATOR,
            SCHEMA_SOFTWARE_APPLICATION, PREFERRED));
    if (relatedResource.startsWith("http")) {
      try {
        entityRelationship.setOdsRelatedResourceURI(new URI(relatedResource));
      } catch (URISyntaxException _) {
        log.warn("Could not create URI for related resource: " + relatedResource);
      }
    }
    return entityRelationship;
  }

}