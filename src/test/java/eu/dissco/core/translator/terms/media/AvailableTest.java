package eu.dissco.core.translator.terms.media;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AvailableTest {

  private final Available available = new Available();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var availableString = "2023-10-01";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:available", availableString);

    // When
    var result = this.available.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(availableString);
  }


  @Test
  void testGetTerm() {
    // When
    var result = available.getTerm();

    // Then
    assertThat(result).isEqualTo(Available.TERM);
  }

}
