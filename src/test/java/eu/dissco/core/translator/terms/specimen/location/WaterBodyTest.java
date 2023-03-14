package eu.dissco.core.translator.terms.specimen.location;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WaterBodyTest {

  private final WaterBody waterBody = new WaterBody();
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var waterBodyString = "The Aegean Sea";
    var unit = new ObjectMapper().createObjectNode();
    unit.put("dwc:waterBody", waterBodyString);

    // When
    var result = waterBody.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(waterBodyString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = waterBody.getTerm();

    // Then
    assertThat(result).isEqualTo(WaterBody.TERM);
  }

}
