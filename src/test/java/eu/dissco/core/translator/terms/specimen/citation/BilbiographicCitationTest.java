package eu.dissco.core.translator.terms.specimen.citation;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.terms.specimen.location.Continent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BilbiographicCitationTest {

  private final BibliographicCitation bibliographicCitation = new BibliographicCitation();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var bibliographicCitationString = "A new bibliographic citation";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:bibliographicCitation", bibliographicCitationString);

    // When
    var result = bibliographicCitation.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(bibliographicCitationString);
  }
  @Test
  void testRetrieveFromABCD() {
    // Given
    var bibliographicCitationString = "A new bibliographic citation";
    var unit = MAPPER.createObjectNode();
    unit.put("titleCitation", bibliographicCitationString);

    // When
    var result = bibliographicCitation.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(bibliographicCitationString);
  }
  @Test
  void testGetTerm() {
    // When
    var result = bibliographicCitation.getTerm();

    // Then
    assertThat(result).isEqualTo(BibliographicCitation.TERM);
  }

}
