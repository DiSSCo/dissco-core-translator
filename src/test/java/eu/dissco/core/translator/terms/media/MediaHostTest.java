package eu.dissco.core.translator.terms.media;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MediaHostTest {

  @Test
  void testGetTerm(){
    // Given
    var result = new MediaHost().getTerm();

    // Then
    assertThat(result).isEqualTo(MediaHost.TERM);
  }

}
