package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubjectPartTest {

  private final SubjectPart subjectPart = new SubjectPart();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var subjectPartString = "http://rs.tdwg.org/acpart/values/p0027";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:subjectPart", subjectPartString);

    // When
    var result = this.subjectPart.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(subjectPartString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = subjectPart.getTerm();

    // Then
    assertThat(result).isEqualTo(SubjectPart.TERM);
  }

}
