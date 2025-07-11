package eu.dissco.core.translator.terms.specimen.assertion;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeasurementValueIRITest {

  private final MeasurementValueIRI measurementValueIRI = new MeasurementValueIRI();

  private final String measurementValueIRIString = "http://vocab.nerc.ac.uk/collection/S11/current/S1152";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("http://rs.iobis.org/obis/terms/measurementValueID", measurementValueIRIString);

    // When
    var result = measurementValueIRI.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(measurementValueIRIString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = measurementValueIRI.getTerm();

    // Then
    assertThat(result).isEqualTo(MeasurementValueIRI.TERM);
  }
}
