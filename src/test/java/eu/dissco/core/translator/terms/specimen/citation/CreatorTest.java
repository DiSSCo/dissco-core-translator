package eu.dissco.core.translator.terms.specimen.citation;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.terms.specimen.location.Continent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContinentTest {

  private final Creator creator = new Creator();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var creatorString = "S. Leeflang";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:creator", creatorString);

    // When
    var result = creator.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(creatorString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = creator.getTerm();

    // Then
    assertThat(result).isEqualTo(Creator.TERM);
  }

}
