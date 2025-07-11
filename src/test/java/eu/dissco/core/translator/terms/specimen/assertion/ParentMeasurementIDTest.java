package eu.dissco.core.translator.terms.specimen.assertion;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParentMeasurementIDTest {

  private final ParentMeasurementID parentMeasurementID = new ParentMeasurementID();

  private final String parentMeasurementIDString = "9c752d22-b09a-11e8-96f8-529269fb1459";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:parentMeasurementID", parentMeasurementIDString);

    // When
    var result = parentMeasurementID.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(parentMeasurementIDString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = parentMeasurementID.getTerm();

    // Then
    assertThat(result).isEqualTo(ParentMeasurementID.TERM);
  }
}
