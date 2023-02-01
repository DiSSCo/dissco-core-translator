package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhysicalSpecimenIdTypeTest {

  private final PhysicalSpecimenIdType physicalSpecimenIdType = new PhysicalSpecimenIdType();

  @Test
  void testGetTerm() {
    // When
    var result = physicalSpecimenIdType.getTerm();

    // Then
    assertThat(result).isEqualTo(PhysicalSpecimenIdType.TERM);

  }
}
