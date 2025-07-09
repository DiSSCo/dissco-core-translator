package eu.dissco.core.translator.terms.specimen.assertion;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeasurementUnitIRITest {

  private final MeasurementUnitIRI measurementUnitIRI = new MeasurementUnitIRI();

  private final String measurementUnitIRIString = "http://vocab.nerc.ac.uk/collection/P06/current/ULAA/";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("http://rs.iobis.org/obis/terms/measurementUnitID", measurementUnitIRIString);

    // When
    var result = measurementUnitIRI.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(measurementUnitIRIString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = measurementUnitIRI.getTerm();

    // Then
    assertThat(result).isEqualTo(MeasurementUnitIRI.TERM);
  }
}
