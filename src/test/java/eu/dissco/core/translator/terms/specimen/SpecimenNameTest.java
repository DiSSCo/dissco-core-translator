package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.schema.OdsHasTaxonIdentification;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpecimenNameTest {

  private static final String SPECIMEN_NAME = "\"Bignonia chica Humb. & Bonpl.\"";
  private final SpecimenName specimenName = new SpecimenName();

  @Test
  void testRetrieve() {
    // Given
    var ds = new eu.dissco.core.translator.schema.DigitalSpecimen()
        .withOdsHasIdentification(List.of(
            new eu.dissco.core.translator.schema.Identification()
                .withOdsIsVerifiedIdentification(Boolean.FALSE)
                .withOdsHasTaxonIdentification(List.of(
                    new OdsHasTaxonIdentification().withDwcScientificName("A very scientific name"))),
            new eu.dissco.core.translator.schema.Identification()
                .withOdsIsVerifiedIdentification(Boolean.TRUE)
                .withOdsHasTaxonIdentification(
                    List.of(new OdsHasTaxonIdentification().withDwcScientificName(SPECIMEN_NAME)))
        ));

    // When
    var result = specimenName.calculate(ds);

    // Then
    assertThat(result).isEqualTo(SPECIMEN_NAME);
  }


  @Test
  void testRetrieveVernacular() {
    // Given
    var ds = new eu.dissco.core.translator.schema.DigitalSpecimen()
        .withOdsHasIdentification(List.of(
            new eu.dissco.core.translator.schema.Identification()
                .withOdsIsVerifiedIdentification(Boolean.TRUE)
                .withOdsHasTaxonIdentification(
                    List.of(new OdsHasTaxonIdentification().withDwcVernacularName(SPECIMEN_NAME)))
        ));

    // When
    var result = specimenName.calculate(ds);

    // Then
    assertThat(result).isEqualTo(SPECIMEN_NAME);
  }

  @Test
  void testRetrieveNoName() {
    // Given
    var ds = new eu.dissco.core.translator.schema.DigitalSpecimen()
        .withOdsHasIdentification(List.of());

    // When
    var result = specimenName.calculate(ds);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void testGetTerm() {
    // When
    var result = specimenName.getTerm();

    // Then
    assertThat(result).isEqualTo(SpecimenName.TERM);
  }
}
