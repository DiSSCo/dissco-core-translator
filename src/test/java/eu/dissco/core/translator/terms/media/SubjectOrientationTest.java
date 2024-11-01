package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubjectOrientationTest {

  private final SubjectOrientation subjectOrientation = new SubjectOrientation();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var subjectOrientationString = "http://rs.tdwg.org/acorient/values/r0002";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:subjectOrientation", subjectOrientationString);

    // When
    var result = this.subjectOrientation.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(subjectOrientationString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = subjectOrientation.getTerm();

    // Then
    assertThat(result).isEqualTo(SubjectOrientation.TERM);
  }

}
