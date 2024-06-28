package eu.dissco.core.translator.terms.specimen.event;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SampleSizeUnitTest {

  private static final String SAMPLE_SIZE_UNIT_STRING = "individuals";

  private final SampleSizeUnit sampleSizeUnit = new SampleSizeUnit();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:sampleSizeUnit", SAMPLE_SIZE_UNIT_STRING);

    // When
    var result = sampleSizeUnit.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(SAMPLE_SIZE_UNIT_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = sampleSizeUnit.getTerm();

    // Then
    assertThat(result).isEqualTo(SampleSizeUnit.TERM);
  }

}
