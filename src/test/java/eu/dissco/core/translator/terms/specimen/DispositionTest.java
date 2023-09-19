package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DispositionTest {

  private static final String DISPOSITION_STRING = "missing";
  private final Disposition disposition = new Disposition();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:disposition", DISPOSITION_STRING);

    // When
    var result = disposition.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(DISPOSITION_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = disposition.getTerm();

    // Then
    assertThat(result).isEqualTo(Disposition.TERM);
  }
}
