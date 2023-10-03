package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccessRightsTest {

  private final AccessRights accessRights = new AccessRights();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var accessRightsString = "Description of accessRights";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:accessRights", accessRightsString);

    // When
    var result = accessRights.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(accessRightsString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = accessRights.getTerm();

    // Then
    assertThat(result).isEqualTo(AccessRights.TERM);
  }

}
