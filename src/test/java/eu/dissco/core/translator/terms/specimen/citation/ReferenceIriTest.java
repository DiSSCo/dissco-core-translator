package eu.dissco.core.translator.terms.specimen.citation;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReferenceIriTest {

  private final ReferenceIri referenceIri = new ReferenceIri();

  @Test
  void testRetrieveFromABCD() {
    // Given
    var referenceIriString = "https://iri-to-reference.com";
    var unit = MAPPER.createObjectNode();
    unit.put("uri", referenceIriString);

    // When
    var result = referenceIri.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(referenceIriString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = referenceIri.getTerm();

    // Then
    assertThat(result).isEqualTo(ReferenceIri.TERM);
  }

}
