package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RecordedByIDTest {

  private static final String RECORDED_BY_ID_STRING = "https://www.wikidata.org/wiki/Q21338018";
  private final RecordedByID recordedBy = new RecordedByID();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:recordedByID", RECORDED_BY_ID_STRING);

    // When
    var result = recordedBy.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(RECORDED_BY_ID_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = recordedBy.getTerm();

    // Then
    assertThat(result).isEqualTo(RecordedByID.TERM);
  }

}
