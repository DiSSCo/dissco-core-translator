package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubtypeLiteralTest {

  private final SubtypeLiteral subtypeLiteral = new SubtypeLiteral();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var subtypeLiteralString = "Photograph";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:subtypeLiteral", subtypeLiteralString);

    // When
    var result = this.subtypeLiteral.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(subtypeLiteralString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = subtypeLiteral.getTerm();

    // Then
    assertThat(result).isEqualTo(SubtypeLiteral.TERM);
  }

}
