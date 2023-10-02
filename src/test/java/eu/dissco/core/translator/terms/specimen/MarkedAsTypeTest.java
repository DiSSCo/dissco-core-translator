package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.Identifications;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarkedAsTypeTest {

  private final MarkedAsType markedAsType = new MarkedAsType();

  @ParameterizedTest
  @ValueSource(strings = {"holotype", "haplotype", "some other type"})
  void testRetrieve(String typeStatus) {
    // Given
    var ds = new DigitalSpecimen().withDwcIdentification(List.of(
        new Identifications()
            .withDwcIdentificationVerificationStatus(Boolean.FALSE),
        new Identifications()
            .withDwcIdentificationVerificationStatus(Boolean.TRUE)
            .withDwcTypeStatus(typeStatus)
    ));

    // When
    var result = markedAsType.calculate(ds);

    // Then
    assertThat(result).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "specimen", "false"})
  void testRetrieveFalse(String typeStatus) {
    // Given
    var ds = new DigitalSpecimen().withDwcIdentification(List.of(
        new Identifications()
            .withDwcIdentificationVerificationStatus(Boolean.FALSE),
        new Identifications()
            .withDwcIdentificationVerificationStatus(Boolean.TRUE)
            .withDwcTypeStatus(typeStatus)
    ));

    // When
    var result = markedAsType.calculate(ds);

    // Then
    assertThat(result).isFalse();
  }

  @Test
  void testRetrieveNull() {
    // Given
    var ds = new DigitalSpecimen().withDwcIdentification(List.of(
        new Identifications()
            .withDwcIdentificationVerificationStatus(Boolean.FALSE)
            .withDwcTypeStatus("holotype"),
        new Identifications()
            .withDwcIdentificationVerificationStatus(Boolean.TRUE)
    ));

    // When
    var result = markedAsType.calculate(ds);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = markedAsType.getTerm();

    // Then
    assertThat(result).isEqualTo(MarkedAsType.TERM);
  }
}
