package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationAccordingToTest {

  private static final String LOCATION_ACCORDING_TO_STRING = "Sam Leeflang";
  private final LocationAccordingTo locationAccordingTo = new LocationAccordingTo();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:locationAccordingTo", LOCATION_ACCORDING_TO_STRING);

    // When
    var result = locationAccordingTo.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(LOCATION_ACCORDING_TO_STRING);
  }


  @Test
  void testGetTerm() {
    // When
    var result = locationAccordingTo.getTerm();

    // Then
    assertThat(result).isEqualTo(LocationAccordingTo.TERM);
  }
}
