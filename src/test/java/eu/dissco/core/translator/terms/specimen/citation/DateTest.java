package eu.dissco.core.translator.terms.specimen.citation;

import static eu.dissco.core.translator.TestUtils.MAPPER;
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
    var dateString = "18-09-2023";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:date", dateString);

    // When
    var result = date.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(dateString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = date.getTerm();

    // Then
    assertThat(result).isEqualTo(Date.TERM);
  }

}
