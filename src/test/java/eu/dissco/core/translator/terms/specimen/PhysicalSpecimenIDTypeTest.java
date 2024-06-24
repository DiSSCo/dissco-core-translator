package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhysicalSpecimenIDTypeTest {

  private final PhysicalSpecimenIDType physicalSpecimenIdType = new PhysicalSpecimenIDType();

  @Test
  void testGetTerm() {
    // When
    var result = physicalSpecimenIdType.getTerm();

    // Then
    assertThat(result).isEqualTo(PhysicalSpecimenIDType.TERM);

  }
}
