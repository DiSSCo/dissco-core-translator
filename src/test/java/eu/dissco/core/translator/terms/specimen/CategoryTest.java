package eu.dissco.core.translator.terms.specimen;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryTest {
  private final Category category = new Category();

  @ParameterizedTest
  @MethodSource("arguments")
  void testRetrieveFromDWCA(String basisOfRecord, String kingdom, String expected) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:basisOfRecord", basisOfRecord);
    unit.put("dwc:kingdom", kingdom);

    // When
    var result = category.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  private static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of("FossilSpecimen", null, "Palaeontology"),
        Arguments.of("MeteoriteSpecimen", null, "Extraterrestrial"),
        Arguments.of("RockSpecimen", null, "Earth System"),
        Arguments.of("PreservedSpecimen", "Animalia", "Zoology"),
        Arguments.of("PreservedSpecimen", "Plantae", "Botany"),
        Arguments.of("PreservedSpecimen", "Bacteria", "Microbiology"),
        Arguments.of("PreservedSpecimen", "incertae sedis", "Unclassified"),
        Arguments.of("Other", null, "Unclassified")
    );
  }

  @ParameterizedTest
  @MethodSource("arguments")
  void testRetrieveFromABCD(String basisOfRecord, String kingdom, String expected) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:recordBasis", basisOfRecord);
    unit.put(
        "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank",
        "regnum");
    unit.put(
        "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName",
        kingdom);

    // When
    var result = category.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testGetTerm() {
    // When
    var result = category.getTerm();

    // Then
    assertThat(result).isEqualTo(Category.TERM);
  }
}
