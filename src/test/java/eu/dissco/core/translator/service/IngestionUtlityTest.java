package eu.dissco.core.translator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import eu.dissco.core.translator.exception.DiSSCoDataException;
import eu.dissco.core.translator.exception.OrganizationNotRorId;
import eu.dissco.core.translator.exception.UnknownPhysicalSpecimenIdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IngestionUtlityTest {

  @Test
  void testCombined() throws DiSSCoDataException {
    // Given
    var type = "combined";
    var organisationId = "https://ror.org/0566bfb96";
    var id = "12345";

    // When
    var result = IngestionUtility.getPhysicalSpecimenId(type, organisationId, id);

    // Then
    assertThat(result).isEqualTo(id + ":0566bfb96");
  }

  @Test
  void testCetaf() throws DiSSCoDataException {
    // Given
    var type = "cetaf";
    var organisationId = "https://ror.org/0566bfb96";
    var id = "https://globally.unique.com/12345";

    // When
    var result = IngestionUtility.getPhysicalSpecimenId(type, organisationId, id);

    // Then
    assertThat(result).isEqualTo(id);
  }

  @Test
  void testInvalidIdType() {
    // Given
    var type = "unknown";
    var organisationId = "https://ror.org/0566bfb96";
    var id = "https://globally.unique.com/12345";

    // When
    var exception = assertThrowsExactly(UnknownPhysicalSpecimenIdType.class,
        () -> IngestionUtility.getPhysicalSpecimenId(type, organisationId, id));

    // Then
    assertThat(exception).isInstanceOf(UnknownPhysicalSpecimenIdType.class);
  }

  @Test
  void testInvalidRoR() {
    // Given
    var type = "combined";
    var organisationId = "0566bfb96";
    var id = "https://globally.unique.com/12345";

    // When
    var exception = assertThrowsExactly(OrganizationNotRorId.class,
        () -> IngestionUtility.getPhysicalSpecimenId(type, organisationId, id));

    // Then
    assertThat(exception).isInstanceOf(OrganizationNotRorId.class);
  }

}
