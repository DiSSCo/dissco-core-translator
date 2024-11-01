package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.Identification;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IsMarkedAsTypeTest {

  private final IsMarkedAsType isMarkedAsType = new IsMarkedAsType();

  @ParameterizedTest
  @ValueSource(strings = {"holotype", "haplotype", "some other type"})
  void testRetrieve(String typeStatus) {
    // Given
    var ds = new DigitalSpecimen().withOdsHasIdentifications(List.of(
        new Identification()
            .withOdsIsVerifiedIdentification(Boolean.FALSE),
        new Identification()
            .withOdsIsVerifiedIdentification(Boolean.TRUE)
            .withDwcTypeStatus(typeStatus)
    ));

    // When
    var result = isMarkedAsType.calculate(ds);

    // Then
    assertThat(result).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "specimen", "false"})
  void testRetrieveFalse(String typeStatus) {
    // Given
    var ds = new DigitalSpecimen().withOdsHasIdentifications(List.of(
        new Identification()
            .withOdsIsVerifiedIdentification(Boolean.FALSE),
        new Identification()
            .withOdsIsVerifiedIdentification(Boolean.TRUE)
            .withDwcTypeStatus(typeStatus)
    ));

    // When
    var result = isMarkedAsType.calculate(ds);

    // Then
    assertThat(result).isFalse();
  }

  @Test
  void testRetrieveNull() {
    // Given
    var ds = new DigitalSpecimen().withOdsHasIdentifications(List.of(
        new Identification()
            .withOdsIsVerifiedIdentification(Boolean.FALSE)
            .withDwcTypeStatus("holotype"),
        new Identification()
            .withOdsIsVerifiedIdentification(Boolean.TRUE)
    ));

    // When
    var result = isMarkedAsType.calculate(ds);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = isMarkedAsType.getTerm();

    // Then
    assertThat(result).isEqualTo(IsMarkedAsType.TERM);
  }
}
