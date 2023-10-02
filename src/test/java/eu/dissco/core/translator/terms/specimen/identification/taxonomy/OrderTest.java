package eu.dissco.core.translator.terms.specimen.identification.taxonomy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTest {

  private final Order order = new Order();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put("dwc:order", "Soricomorpha");

    // When
    var result = order.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo("Soricomorpha");
  }

  @Test
  void testRetrieveFromABCD() {
    // Given
    var unit = MAPPER.createObjectNode();
    unit.put(
        "result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank",
        "ordo");
    unit.put(
        "result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName",
        "Soricomorpha");

    // When
    var result = order.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo("Soricomorpha");
  }

  @Test
  void testGetTerm() {
    // When
    var result = order.getTerm();

    // Then
    assertThat(result).isEqualTo(Order.TERM);
  }
}
