package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WaterBodyTest {

  private final WaterBody waterBody = new WaterBody();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var waterBodyString = "The Aegean Sea";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:waterBody", waterBodyString);

    // When
    var result = waterBody.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(waterBodyString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = waterBody.getTerm();

    // Then
    assertThat(result).isEqualTo(WaterBody.TERM);
  }

}
