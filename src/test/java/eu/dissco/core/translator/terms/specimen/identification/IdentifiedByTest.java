package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdentifiedByTest {

  private final DateIdentified dateIdentified = new DateIdentified();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var dateIdentifiedString = "18-09-2023";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:dateIdentified", dateIdentifiedString);

    // When
    var result = dateIdentified.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(dateIdentifiedString);
  }
  @Test
  void testRetrieveFromABCD() {
    // Given
    var dateIdentifiedString = "18-09-2023";
    var unit = MAPPER.createObjectNode();
    unit.put("date/dateText", dateIdentifiedString);

    // When
    var result = dateIdentified.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(dateIdentifiedString);
  }
  @Test
  void testGetTerm() {
    // When
    var result = dateIdentified.getTerm();

    // Then
    assertThat(result).isEqualTo(DateIdentified.TERM);
  }

}
