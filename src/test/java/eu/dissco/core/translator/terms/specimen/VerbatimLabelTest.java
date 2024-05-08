package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerbatimLabelTest {

  private static final String VERBATIM_LABEL_STRING = "https://www.wikidata.org/wiki/Q21338018";
  private final VerbatimLabel verbatimLabel = new VerbatimLabel();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:verbatimLabel", VERBATIM_LABEL_STRING);

    // When
    var result = verbatimLabel.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM_LABEL_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:specimenUnit/marks/mark/0/markText/value", VERBATIM_LABEL_STRING);

    // When
    var result = verbatimLabel.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(VERBATIM_LABEL_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = verbatimLabel.getTerm();

    // Then
    assertThat(result).isEqualTo(VerbatimLabel.TERM);
  }

}
