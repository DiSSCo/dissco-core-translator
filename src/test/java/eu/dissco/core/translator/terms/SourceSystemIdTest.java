package eu.dissco.core.translator.terms;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SourceSystemIdTest {

  @Test
  void testGetTerm() {
    // When
    var result = new SourceSystemId().getTerm();

    // Then
    assertThat(result).isEqualTo(SourceSystemId.TERM);
  }

}
