package eu.dissco.core.translator.terms.specimen.location;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationRemarksTest {

  private static final String LOCATION_REMARKS_STRING = "Remarks about the location";
  private final LocationRemarks locationRemarks = new LocationRemarks();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:locationRemarks", LOCATION_REMARKS_STRING);

    // When
    var result = locationRemarks.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(LOCATION_REMARKS_STRING);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:gathering/areaDetail/value",
        LOCATION_REMARKS_STRING);

    // When
    var result = locationRemarks.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(LOCATION_REMARKS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = locationRemarks.getTerm();

    // Then
    assertThat(result).isEqualTo(LocationRemarks.TERM);
  }
}
