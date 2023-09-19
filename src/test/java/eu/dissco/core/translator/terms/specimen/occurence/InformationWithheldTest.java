package eu.dissco.core.translator.terms.specimen.occurence;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.jooq.impl.QOM.In;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InformationWithheldTest {

  private static final String INFORMATION_WITHHELD_STRING = "Yes";

  private final InformationWithheld informationWithheld = new InformationWithheld();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:informationWithheld", INFORMATION_WITHHELD_STRING);

    // When
    var result = informationWithheld.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(INFORMATION_WITHHELD_STRING);
  }
  @Test
  void testGetTerm() {
    // When
    var result = informationWithheld.getTerm();

    // Then
    assertThat(result).isEqualTo(InformationWithheld.TERM);
  }

}
