package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ModifiedTest {

  private final Modified modified = new Modified();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var licenseString = "23-03-1989";
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:modified", licenseString);

    // When
    var result = modified.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(licenseString);
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    String modifiedString = "1674553668909";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:dateLastEdited", modifiedString);

    // When
    var result = modified.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(modifiedString);
  }

  @Test
  void testRetrieveFromABCDEmpty() {
    // Given
    var unit = MAPPER.createObjectNode();

    // When
    var result = modified.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = modified.getTerm();

    // Then
    assertThat(result).isEqualTo(Modified.TERM);
  }

}
