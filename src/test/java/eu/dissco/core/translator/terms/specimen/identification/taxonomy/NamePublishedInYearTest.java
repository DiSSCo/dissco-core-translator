package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NamePublishedInYearTest {

  private final NamePublishedInYear namePublishedInYear = new NamePublishedInYear();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var namePublishedInYearString = "2023";
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:namePublishedInYear", namePublishedInYearString);

    // When
    var result = namePublishedInYear.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(namePublishedInYearString);
  }

  @Test
  void testGetTerm() {
    // When
    var result = namePublishedInYear.getTerm();

    // Then
    assertThat(result).isEqualTo(NamePublishedInYear.TERM);
  }
}
