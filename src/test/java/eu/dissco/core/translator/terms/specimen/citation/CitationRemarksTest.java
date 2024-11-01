package eu.dissco.core.translator.terms.specimen.citation;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CitationRemarksTest {

  private final CitationDescription citationDescription = new CitationDescription();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var citationDescriptionString = "A new citation remark";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:description", citationDescriptionString);

    // When
    var result = citationDescription.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(citationDescriptionString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var citationDescriptionString = "A new citation remark";
    var unit = MAPPER.createObjectNode();
    unit.put("citationDetail", citationDescriptionString);

    // When
    var result = citationDescription.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(citationDescriptionString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = citationDescription.getTerm();

    // Then
    assertThat(result).isEqualTo(CitationDescription.TERM);
  }

}
