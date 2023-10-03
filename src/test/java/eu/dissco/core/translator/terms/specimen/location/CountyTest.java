package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountyTest {

  private static final String COUNTY_STRING = "Northumberland";
  private final County county = new County();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:county", COUNTY_STRING);

    // When
    var result = county.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(COUNTY_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaClass/value", "County");
    unit.put("abcd:gathering/namedAreas/namedArea/0/areaName/value", COUNTY_STRING);

    // When
    var result = county.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(COUNTY_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = county.getTerm();

    // Then
    assertThat(result).isEqualTo(County.TERM);
  }

}
