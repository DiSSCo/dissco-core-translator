package eu.dissco.core.translator.terms.specimen.location;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CountyTest {

  private final County county = new County();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var countyString = "Northumberland";
    var unit = new ObjectMapper().createObjectNode();
    unit.put("dwc:county", countyString);

    // When
    var result = county.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(countyString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = county.getTerm();

    // Then
    assertThat(result).isEqualTo(County.TERM);
  }

}
