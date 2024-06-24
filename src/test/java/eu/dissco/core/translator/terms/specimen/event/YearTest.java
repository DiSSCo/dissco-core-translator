package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class YearTest {

  private static final String YEAR_STRING = "2024";

  private final Year year = new Year();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:year", YEAR_STRING);

    // When
    var result = year.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(YEAR_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = year.getTerm();

    // Then
    assertThat(result).isEqualTo(Year.TERM);
  }

}
