package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MOCK_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.terms.specimen.citation.BibliographicCitation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DateIdentifiedTest {

  private final DateIdentified dateIdentified = new DateIdentified();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:dateIdentified", MOCK_DATE);

    // When
    var result = dateIdentified.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MOCK_DATE);
  }
  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("date/dateText", MOCK_DATE);

    // When
    var result = dateIdentified.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(MOCK_DATE);
  }
  @Test
  void testGetTerm() {
    // When
    var result = dateIdentified.getTerm();

    // Then
    assertThat(result).isEqualTo(DateIdentified.TERM);
  }

}
