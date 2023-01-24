package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TypeTest {

  private final Type type = new Type();

  @Test
  void testGetTerm(){
    // When
    var result = type.getTerm();

    // Then
    assertThat(result).isEqualTo(Type.TERM);

  }
}
