package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.schema.TaxonIdentification;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpecimenNameTest {

  private static final String SPECIMEN_NAME = "\"Bignonia chica Humb. & Bonpl.\"";
  private final SpecimenName specimenName = new SpecimenName();

  @Test
  void testRetrieveFromDWCA() {
    // Given
    var ds = new eu.dissco.core.translator.schema.DigitalSpecimen()
        .withDwcIdentification(List.of(
            new eu.dissco.core.translator.schema.Identifications()
                .withDwcIdentificationVerificationStatus(Boolean.FALSE)
                .withTaxonIdentifications(List.of(
                    new TaxonIdentification().withDwcScientificName("A very scientific name"))),
            new eu.dissco.core.translator.schema.Identifications()
                .withDwcIdentificationVerificationStatus(Boolean.TRUE)
                .withTaxonIdentifications(
                    List.of(new TaxonIdentification().withDwcScientificName(SPECIMEN_NAME)))
        ));

    // When
    var result = specimenName.calculate(ds);

    // Then
    assertThat(result).isEqualTo(SPECIMEN_NAME);
  }

  @Test
  void testGetTerm() {
    // When
    var result = specimenName.getTerm();

    // Then
    assertThat(result).isEqualTo(SpecimenName.TERM);
  }
}
