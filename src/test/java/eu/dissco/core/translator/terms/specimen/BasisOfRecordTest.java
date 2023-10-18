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
class BasisOfRecordTest {

  private final BasisOfRecord basisOfRecord = new BasisOfRecord();

  public static Stream<Arguments> prepareABCDRecordBasis() {
    return Stream.of(
        Arguments.of("PreservedSpecimen", "PreservedSpecimen"),
        Arguments.of("RockSpecimen", "RockSpecimen"),
        Arguments.of("HerbariumSheet", "Preserved specimen"),
        Arguments.of("Dried", "Preserved specimen"),
        Arguments.of(null, null)
    );
  }

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:basisOfRecord", "LivingSpecimen");

    // When
    var result = basisOfRecord.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("LivingSpecimen");
  }

  @ParameterizedTest
  @MethodSource("prepareABCDRecordBasis")
  void testRetrieveFromABCD(String recordBasis, String expected) {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("abcd:recordBasis", recordBasis);

    // When
    var result = basisOfRecord.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(expected);
  }


  @Test
  void testGetTerm() {
    // When
    var result = basisOfRecord.getTerm();

    // Then
    assertThat(result).isEqualTo(BasisOfRecord.TERM);
  }
}
