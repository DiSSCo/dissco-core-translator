package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RightsHolderTest {

  private final Rights rights = new Rights();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var rightsString = "Some rights";
    var unit = MAPPER.createObjectNode();
    unit.put("dc:rights", rightsString);

    // When
    var result = rights.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(rightsString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = rights.getTerm();

    // Then
    assertThat(result).isEqualTo(Rights.TERM);
  }

}
