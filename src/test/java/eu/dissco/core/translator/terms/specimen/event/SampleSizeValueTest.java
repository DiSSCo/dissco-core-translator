package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SampleSizeValueTest {

  private static final String SAMPLE_SIZE_STRING = "3";

  private final SampleSizeValue sampleSizeValue = new SampleSizeValue();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:sampleSizeValue", SAMPLE_SIZE_STRING);

    // When
    var result = sampleSizeValue.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(SAMPLE_SIZE_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = sampleSizeValue.getTerm();

    // Then
    assertThat(result).isEqualTo(SampleSizeValue.TERM);
  }

}
