package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhysicalSpecimenIDTest {

  private final PhysicalSpecimenID physicalSpecimenId = new PhysicalSpecimenID();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var id = "123456789";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:occurrenceID", id);

    // When
    var result = physicalSpecimenId.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(id);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var id = "123456789";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:unitID", id);

    // When
    var result = physicalSpecimenId.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(id);
  }

  @Test
  void testGetTerm() {
    // When
    var result = physicalSpecimenId.getTerm();

    // Then
    assertThat(result).isEqualTo(PhysicalSpecimenID.TERM);
  }

}
