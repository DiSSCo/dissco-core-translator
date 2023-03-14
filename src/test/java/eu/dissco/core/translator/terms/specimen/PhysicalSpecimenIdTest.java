package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhysicalSpecimenIdTest {

  private final PhysicalSpecimenId physicalSpecimenId = new PhysicalSpecimenId();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var id = "123456789";
    var unit = new ObjectMapper().createObjectNode();
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
    var unit = new ObjectMapper().createObjectNode();
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
    assertThat(result).isEqualTo(PhysicalSpecimenId.TERM);
  }

}
