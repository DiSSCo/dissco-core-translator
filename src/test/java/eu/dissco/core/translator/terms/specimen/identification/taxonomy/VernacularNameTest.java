package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VernacularNameTest {

  private final VernacularName vernacularName = new VernacularName();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var vernacularNameString = "A vernacular name";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:vernacularName", vernacularNameString);

    // When
    var result = vernacularName.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(vernacularNameString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = vernacularName.getTerm();

    // Then
    assertThat(result).isEqualTo(VernacularName.TERM);
  }
}
