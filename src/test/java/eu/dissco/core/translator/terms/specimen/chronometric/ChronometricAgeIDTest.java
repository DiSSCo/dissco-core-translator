package eu.dissco.core.translator.terms.specimen.chronometric;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChronometricAgeIDTest {

  private static final String AGE_ID = "https://www.canadianarchaeology.ca/samples/70673";
  private final ChronometricAgeID chronometricAgeID = new ChronometricAgeID();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("chrono:chronometricAgeID", AGE_ID);

    // When
    var result = chronometricAgeID.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(AGE_ID);
  }

  @Test
  void testGetTerm() {
    // When
    var result = chronometricAgeID.getTerm();

    // Then
    assertThat(result).isEqualTo(ChronometricAgeID.TERM);
  }
}
