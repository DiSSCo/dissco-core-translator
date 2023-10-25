package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CollectionIdTest {

  private final CollectionId collectionId = new CollectionId();

  private final String collectionIdString = "collection-123456";

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:collectionID", collectionIdString);

    // When
    var result = collectionId.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(collectionIdString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:sourceID", collectionIdString);

    // When
    var result = collectionId.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(collectionIdString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = collectionId.getTerm();

    // Then
    assertThat(result).isEqualTo(CollectionId.TERM);
  }
}
