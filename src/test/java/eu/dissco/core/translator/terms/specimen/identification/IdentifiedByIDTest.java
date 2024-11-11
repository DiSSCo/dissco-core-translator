package eu.dissco.core.translator.terms.specimen.identification;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdentifiedByIDTest {

  private final IdentifiedByID identifiedById = new IdentifiedByID();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var identifiedByString = "https://orcid.org/0000-0002-5669-2769";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:identifiedByID", identifiedByString);

    // When
    var result = identifiedById.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(identifiedByString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = identifiedById.getTerm();

    // Then
    assertThat(result).isEqualTo(IdentifiedByID.TERM);
  }

}
