package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MonthTest {

  private static final String MONTH_STRING = "3";

  private final Month month = new Month();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:month", MONTH_STRING);

    // When
    var result = month.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MONTH_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = month.getTerm();

    // Then
    assertThat(result).isEqualTo(Month.TERM);
  }

}
