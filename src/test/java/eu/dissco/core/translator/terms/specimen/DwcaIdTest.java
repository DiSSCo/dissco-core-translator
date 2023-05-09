package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DwcaIdTest {

  private final DwcaId dwcaId = new DwcaId();

  @Test
  void testGetTerm() {
    // When
    var result = dwcaId.getTerm();

    // Then
    assertThat(result).isEqualTo(DwcaId.TERM);

  }
}
