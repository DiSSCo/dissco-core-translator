package eu.dissco.core.translator.terms.specimen.citation;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MOCK_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DateTest {

  private final Date date = new Date();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:date", MOCK_DATE);

    // When
    var result = date.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MOCK_DATE);
  }

  @Test
  void testGetTerm() {
    // When
    var result = date.getTerm();

    // Then
    assertThat(result).isEqualTo(Date.TERM);
  }

}
