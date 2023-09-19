package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaxonomicStatusTest {

  private final TaxonomicStatus taxonomicStatus = new TaxonomicStatus();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var taxonomicStatusString = "accepted";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:taxonomicStatus", taxonomicStatusString);

    // When
    var result = taxonomicStatus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(taxonomicStatusString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = taxonomicStatus.getTerm();

    // Then
    assertThat(result).isEqualTo(TaxonomicStatus.TERM);
  }
}
