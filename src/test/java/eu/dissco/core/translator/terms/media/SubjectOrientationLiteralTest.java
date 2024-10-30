package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubjectOrientationLiteralTest {

  private final SubjectOrientationLiteral subjectOrientationLiteral = new SubjectOrientationLiteral();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var subjectOrientationLiteralString = "Lateral";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:subjectOrientationLiteral", subjectOrientationLiteralString);

    // When
    var result = this.subjectOrientationLiteral.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(subjectOrientationLiteralString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = subjectOrientationLiteral.getTerm();

    // Then
    assertThat(result).isEqualTo(SubjectOrientationLiteral.TERM);
  }

}
