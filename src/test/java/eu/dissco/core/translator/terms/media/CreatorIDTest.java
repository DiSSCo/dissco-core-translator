package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreatorIDTest {

  private final CreatorID creatorID = new CreatorID();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var creatorIDString = "https://orcid.org/0000-0002-5669-2769";
    var unit = MAPPER.createObjectNode();
    unit.put("dc:creator", creatorIDString);

    // When
    var result = creatorID.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(creatorIDString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = creatorID.getTerm();

    // Then
    assertThat(result).isEqualTo(CreatorID.TERM);
  }

}
