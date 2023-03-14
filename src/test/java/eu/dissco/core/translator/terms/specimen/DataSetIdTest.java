package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataSetIdTest {

  private final DatasetId datasetId = new DatasetId();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var datasetIdString = "datasetId-123456";
    var unit = new ObjectMapper().createObjectNode();
    unit.put("dwc:datasetID", datasetIdString);

    // When
    var result = datasetId.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(datasetIdString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = datasetId.getTerm();

    // Then
    assertThat(result).isEqualTo(DatasetId.TERM);
  }
}
