package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationIdTest {

  private static final String LOCATION_ID = "https://opencontext.org/subjects/768A875F-E205-4D0B-DE55-BAB7598D0FD1";

  private final LocationId locationId = new LocationId();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:locationID", LOCATION_ID);

    // When
    var result = locationId.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(LOCATION_ID);
  }

  @Test
  void testGetTerm() {
    // When
    var result = locationId.getTerm();

    // Then
    assertThat(result).isEqualTo(LocationId.TERM);
  }
}
