package eu.dissco.core.translator.terms.specimen.occurence;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EstablishmentMeansTest {

  private static final String ESTABLISHMENT_MEANS_STRING = "http://rs.tdwg.org/dwcem/values/e001";

  private final EstablishmentMeans establishmentMeans = new EstablishmentMeans();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:establishmentMeans", ESTABLISHMENT_MEANS_STRING);

    // When
    var result = establishmentMeans.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(ESTABLISHMENT_MEANS_STRING);
  }
  @Test
  void testGetTerm() {
    // When
    var result = establishmentMeans.getTerm();

    // Then
    assertThat(result).isEqualTo(EstablishmentMeans.TERM);
  }

}
