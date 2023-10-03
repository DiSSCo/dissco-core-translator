package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MOCK_DATE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoreferencedDateTest {

  private final GeoreferencedDate georeferencedDate = new GeoreferencedDate();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:georeferencedDate", MOCK_DATE);

    // When
    var result = georeferencedDate.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(MOCK_DATE);
  }

  @Test
  void testGetTerm() {
    // When
    var result = georeferencedDate.getTerm();

    // Then
    assertThat(result).isEqualTo(GeoreferencedDate.TERM);
  }

}
