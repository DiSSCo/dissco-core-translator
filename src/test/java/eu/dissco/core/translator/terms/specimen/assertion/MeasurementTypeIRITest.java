package eu.dissco.core.translator.terms.specimen.assertion;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MeasurementTypeIRITest {

  private final MeasurementTypeIRI measurementType = new MeasurementTypeIRI();

  private final String measurementTypeIRIString = "vocab.nerc.ac.uk/collection/P01/current/OCOUNT01";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("http://rs.iobis.org/obis/terms/measurementTypeID", measurementTypeIRIString);

    // When
    var result = measurementType.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(measurementTypeIRIString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = measurementType.getTerm();

    // Then
    assertThat(result).isEqualTo(MeasurementTypeIRI.TERM);
  }
}
