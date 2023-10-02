package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MOCK_DATE;
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
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:created", MOCK_DATE);

    // When
    var result = created.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MOCK_DATE);
  }

  @Test
  void testRetrieveFromAbcd() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:createdDate", MOCK_DATE);

    // When
    var result = created.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MOCK_DATE);
  }


  @Test
  void testGetTerm() {
    // When
    var result = created.getTerm();

    // Then
    assertThat(result).isEqualTo(Created.TERM);
  }

}
