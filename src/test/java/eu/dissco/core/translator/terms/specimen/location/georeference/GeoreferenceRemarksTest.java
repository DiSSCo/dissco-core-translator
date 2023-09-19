package eu.dissco.core.translator.terms.specimen.location.georeference;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoreferenceRemarksTest {

  private static final String GEOREFERENCE_REMARKS = "Assumed distance by road (Hwy. 101)";

  private final GeoreferenceRemarks georeferenceRemarks = new GeoreferenceRemarks();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:georeferenceRemarks", GEOREFERENCE_REMARKS);

    // When
    var result = georeferenceRemarks.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(GEOREFERENCE_REMARKS);
  }

  @Test
  void testGetTerm() {
    // When
    var result = georeferenceRemarks.getTerm();

    // Then
    assertThat(result).isEqualTo(GeoreferenceRemarks.TERM);
  }

}
