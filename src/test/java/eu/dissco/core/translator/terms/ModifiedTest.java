package eu.dissco.core.translator.terms;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.MOCK_DATE;
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
    var licenseString = MOCK_DATE;
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:modified", licenseString);

    // When
    var result = modified.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(licenseString);
  }

  @Test
  void testRetrieveFromDWCAEmpty() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dcterms:modified", "    ");

    // When
    var result = modified.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isNotBlank();
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    String modifiedString = "1674553668909";
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:dateLastEdited", modifiedString);
    unit.put("abcd:metadata/revisionData/dateModified", "1604521759000");

    // When
    var result = modified.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(modifiedString);
  }

  @Test
  void testRetrieveFromABCDFromMeta() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:revisionData/dateModified", "1604521759000");

    // When
    var result = modified.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("1604521759000");
  }

  @Test
  void testRetrieveFromABCDFromMetaLong() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:revisionData/dateModified", 1604521759000L);

    // When
    var result = modified.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("1604521759000");
  }

  @Test
  void testRetrieveFromABCDEmpty() {
    // Given
    var unit = MAPPER.createObjectNode();

    // When
    var result = modified.retrieveFromABCD(unit);

    // Then
    assertThat(result).isNotBlank();
  }

  @Test
  void testGetTerm() {
    // When
    var result = modified.getTerm();

    // Then
    assertThat(result).isEqualTo(Modified.TERM);
  }

}
