package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AcceptedNameUsageIdTest {

  private final AcceptedNameUsageId acceptedNameUsageId = new AcceptedNameUsageId();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var acceptedNameUsageIdString = "An acceptedNameUsage Identifier";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:acceptedNameUsageId", acceptedNameUsageIdString);

    // When
    var result = acceptedNameUsageId.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(acceptedNameUsageIdString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = acceptedNameUsageId.getTerm();

    // Then
    assertThat(result).isEqualTo(AcceptedNameUsageId.TERM);
  }
}
