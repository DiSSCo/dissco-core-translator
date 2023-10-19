package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OriginalNameUsageTest {

  private final OriginalNameUsage originalNameUsage = new OriginalNameUsage();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var originalNameUsageString = "Pinus abies";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:originalNameUsage", originalNameUsageString);

    // When
    var result = originalNameUsage.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(originalNameUsageString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = originalNameUsage.getTerm();

    // Then
    assertThat(result).isEqualTo(OriginalNameUsage.TERM);
  }
}
