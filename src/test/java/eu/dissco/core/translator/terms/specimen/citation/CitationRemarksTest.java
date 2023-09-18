package eu.dissco.core.translator.terms.specimen.citation;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CitationRemarksTest {

  private final CitationRemarks citationRemarks = new CitationRemarks();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var citationRemarksString = "A new citation remark";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:taxonRemarks", citationRemarksString);

    // When
    var result = citationRemarks.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(citationRemarksString);
  }
  @Test
  void testRetrieveFromABCD() {
    // Given
    var citationRemarksString = "A new citation remark";
    var unit = MAPPER.createObjectNode();
    unit.put("citationDetail", citationRemarksString);

    // When
    var result = citationRemarks.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(citationRemarksString);
  }
  @Test
  void testGetTerm() {
    // When
    var result = citationRemarks.getTerm();

    // Then
    assertThat(result).isEqualTo(CitationRemarks.TERM);
  }

}
