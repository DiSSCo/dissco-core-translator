package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubtypeTest {

  private final Subtype subtype = new Subtype();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var subtypeString = "http://rs.tdwg.org/acsubtype/values/Photograph";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:subtype", subtypeString);

    // When
    var result = this.subtype.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(subtypeString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = subtype.getTerm();

    // Then
    assertThat(result).isEqualTo(Subtype.TERM);
  }

}
