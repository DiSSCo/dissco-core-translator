package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatasetNameTest {

  private static final String DATASET_NAME_STRING = "Grinnell Resurvey Mammals";
  private final DatasetName datasetName = new DatasetName();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:datasetName", DATASET_NAME_STRING);

    // When
    var result = datasetName.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(DATASET_NAME_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = datasetName.getTerm();

    // Then
    assertThat(result).isEqualTo(DatasetName.TERM);
  }
}
