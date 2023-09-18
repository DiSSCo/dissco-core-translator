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
    var accessUriString = "https://accessuri.eu/image_1";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:accessRights", accessUriString);

    // When
    var result = accessRights.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(accessUriString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = accessRights.getTerm();

    // Then
    assertThat(result).isEqualTo(AccessRights.TERM);
  }

}
