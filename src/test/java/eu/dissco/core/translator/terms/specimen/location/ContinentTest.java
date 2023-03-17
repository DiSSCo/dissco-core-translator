package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContinentTest {

  private final Continent continent = new Continent();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var continentString = "Europe";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:continent", continentString);

    // When
    var result = continent.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(continentString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = continent.getTerm();

    // Then
    assertThat(result).isEqualTo(Continent.TERM);
  }

}
