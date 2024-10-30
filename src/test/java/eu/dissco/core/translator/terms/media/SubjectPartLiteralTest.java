package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubjectPartLiteralTest {

  private final SubjectPartLiteral subjectPartLiteral = new SubjectPartLiteral();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var subjectPartLiteralString = "Head";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:subjectPartLiteral", subjectPartLiteralString);

    // When
    var result = this.subjectPartLiteral.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(subjectPartLiteralString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = subjectPartLiteral.getTerm();

    // Then
    assertThat(result).isEqualTo(SubjectPartLiteral.TERM);
  }

}
