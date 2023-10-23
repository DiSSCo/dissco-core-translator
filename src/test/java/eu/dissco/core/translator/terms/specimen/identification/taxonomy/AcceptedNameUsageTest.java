package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AcceptedNameUsageTest {

  private final AcceptedNameUsage acceptedNameUsage = new AcceptedNameUsage();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var acceptedNameUsageString = "An acceptedNameUsage";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:acceptedNameUsage", acceptedNameUsageString);

    // When
    var result = acceptedNameUsage.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(acceptedNameUsageString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = acceptedNameUsage.getTerm();

    // Then
    assertThat(result).isEqualTo(AcceptedNameUsage.TERM);
  }
}
