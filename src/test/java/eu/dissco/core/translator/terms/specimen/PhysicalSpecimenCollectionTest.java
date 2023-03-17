package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PhysicalSpecimenCollectionTest {

  private final PhysicalSpecimenCollection physicalSpecimenCollection = new PhysicalSpecimenCollection();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var collectionString = "collection-123456";
    var unit = MAPPER.createObjectNode();
    unit.putNull("dwc:collectionID");
    unit.put("dwc:collectionCode", collectionString);

    // When
    var result = physicalSpecimenCollection.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(collectionString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = physicalSpecimenCollection.getTerm();

    // Then
    assertThat(result).isEqualTo(PhysicalSpecimenCollection.TERM);
  }
}
