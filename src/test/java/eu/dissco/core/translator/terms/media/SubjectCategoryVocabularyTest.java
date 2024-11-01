package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubjectCategoryVocabularyTest {

  private final SubjectCategoryVocabulary subjectCategoryVocabulary = new SubjectCategoryVocabulary();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var subjectCategoryVocabularyString = "http://rs.tdwg.org/accontent/values";
    var unit = MAPPER.createObjectNode();
    unit.put("ac:subjectCategoryVocabulary", subjectCategoryVocabularyString);

    // When
    var result = this.subjectCategoryVocabulary.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(subjectCategoryVocabularyString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = subjectCategoryVocabulary.getTerm();

    // Then
    assertThat(result).isEqualTo(SubjectCategoryVocabulary.TERM);
  }

}
