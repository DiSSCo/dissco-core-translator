package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NameAccordingToTest {

  private final NameAccordingTo nameAccordingTo = new NameAccordingTo();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:nameAccordingTo", "COL");

    // When
    var result = nameAccordingTo.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("COL");
  }

  @Test
  void testGetTerm() {
    // When
    var result = nameAccordingTo.getTerm();

    // Then
    assertThat(result).isEqualTo(NameAccordingTo.TERM);
  }
}
