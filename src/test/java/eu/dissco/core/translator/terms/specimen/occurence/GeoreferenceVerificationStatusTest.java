package eu.dissco.core.translator.terms.specimen.occurence;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GeoreferenceVerificationStatusTest {

  private static final String GEOREFERENCE_VERIFICATION_STATUS_STRING = "confirmed";

  private final GeoreferenceVerificationStatus georeferenceVerificationStatus = new GeoreferenceVerificationStatus();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:georeferenceVerificationStatus", GEOREFERENCE_VERIFICATION_STATUS_STRING);

    // When
    var result = georeferenceVerificationStatus.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(GEOREFERENCE_VERIFICATION_STATUS_STRING);
  }

  @Test
  void testGetTerm() {
    // When
    var result = georeferenceVerificationStatus.getTerm();

    // Then
    assertThat(result).isEqualTo(GeoreferenceVerificationStatus.TERM);
  }

}
