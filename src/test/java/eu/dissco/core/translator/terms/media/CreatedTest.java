package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreatedTest {

  private final Created created = new Created();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var createdString = "18-09-2023";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:created", createdString);

    // When
    var result = created.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(createdString);
  }

  @Test
  void testRetrieveFromAbcd() {
    // Given
    var createdString = "18-09-2023";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:createdDate", createdString);

    // When
    var result = created.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(createdString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = created.getTerm();

    // Then
    assertThat(result).isEqualTo(Created.TERM);
  }

}
