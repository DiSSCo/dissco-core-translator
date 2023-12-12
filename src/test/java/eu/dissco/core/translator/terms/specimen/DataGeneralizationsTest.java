package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataGeneralizationsTest {

  private static final String DATA_GENERALIZATIONS_STRING = "The data was generalised";

  private final DataGeneralizations dataGeneralizations = new DataGeneralizations();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:dataGeneralizations", DATA_GENERALIZATIONS_STRING);

    // When
    var result = dataGeneralizations.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(DATA_GENERALIZATIONS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = dataGeneralizations.getTerm();

    // Then
    assertThat(result).isEqualTo(DataGeneralizations.TERM);
  }

}
