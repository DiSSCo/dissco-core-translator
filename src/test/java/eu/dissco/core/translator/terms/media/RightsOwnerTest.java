package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RightsOwnerTest {

  private final RightsOwner rightsOwner = new RightsOwner();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var rightsOwnerString = "Naturalis Biodiversity Center";
    var unit = MAPPER.createObjectNode();
    unit.put("xmpRights:Owner", rightsOwnerString);

    // When
    var result = this.rightsOwner.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(rightsOwnerString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = rightsOwner.getTerm();

    // Then
    assertThat(result).isEqualTo(RightsOwner.TERM);
  }

}
