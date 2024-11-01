package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class Iptc4xmpExtCVtermTest {

  private final Iptc4xmpExtCVterm iptc4xmpExtCVterm = new Iptc4xmpExtCVterm();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var cvTermString = "http://rs.tdwg.org/accontent/values/c000";
    var unit = MAPPER.createObjectNode();
    unit.put("Iptc4xmpExt:CVterm", cvTermString);

    // When
    var result = this.iptc4xmpExtCVterm.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(cvTermString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = iptc4xmpExtCVterm.getTerm();

    // Then
    assertThat(result).isEqualTo(Iptc4xmpExtCVterm.TERM);
  }

}
