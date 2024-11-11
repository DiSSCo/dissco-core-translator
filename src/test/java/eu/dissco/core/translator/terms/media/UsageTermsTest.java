package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsageTermsTest {

  private final UsageTerms usageTerms = new UsageTerms();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var usageTermsString = "CC BY-NC-SA 4.0";
    var unit = MAPPER.createObjectNode();
    unit.put("xmpRights:UsageTerms", usageTermsString);

    // When
    var result = usageTerms.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(usageTermsString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = usageTerms.getTerm();

    // Then
    assertThat(result).isEqualTo(UsageTerms.TERM);
  }

}
