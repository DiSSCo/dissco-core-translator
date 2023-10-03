package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccessRightsTest {

  private static final String ACCESS_RIGHTS_STRING = "S. Leeflang";
  private final AccessRights accessRights = new AccessRights();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:accessRights", ACCESS_RIGHTS_STRING);

    // When
    var result = accessRights.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(ACCESS_RIGHTS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = accessRights.getTerm();

    // Then
    assertThat(result).isEqualTo(AccessRights.TERM);
  }
}
