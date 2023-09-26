package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.schema.DigitalSpecimen;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsTopicDiscipline;
import eu.dissco.core.translator.schema.TaxonIdentification;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TopicDisciplineTest {

  private final TopicDiscipline topicDiscipline = new TopicDiscipline();

  private static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of(givenDigitalSpecimen("FossilSpecimen", null),
            OdsTopicDiscipline.PALAEONTOLOGY),
        Arguments.of(givenDigitalSpecimen("MeteoriteSpecimen", null),
            OdsTopicDiscipline.ASTROGEOLOGY),
        Arguments.of(givenDigitalSpecimen("RockSpecimen", null),
            OdsTopicDiscipline.EARTH_GEOLOGY),
        Arguments.of(givenDigitalSpecimen("PreservedSpecimen", "Animalia"),
            OdsTopicDiscipline.ZOOLOGY),
        Arguments.of(givenDigitalSpecimen("PreservedSpecimen", "Plantae"),
            OdsTopicDiscipline.BOTANY),
        Arguments.of(givenDigitalSpecimen("PreservedSpecimen", "Bacteria"),
            OdsTopicDiscipline.MICROBIOLOGY),
        Arguments.of(givenDigitalSpecimen("PreservedSpecimen", "incertae sedis"),
            OdsTopicDiscipline.UNCLASSIFIED),
        Arguments.of(givenDigitalSpecimen("Other", null), OdsTopicDiscipline.UNCLASSIFIED)
    );
  }

  private static Object givenDigitalSpecimen(String basisOfRecord, String kingdom) {
    return new DigitalSpecimen()
        .withDwcBasisOfRecord(basisOfRecord)
        .withDwcIdentification(List.of(
                new eu.dissco.core.translator.schema.Identifications()
                    .withDwcIdentificationVerificationStatus(false)
                    .withTaxonIdentifications(
                        List.of(new TaxonIdentification().withDwcKingdom("Unicorn Kingdom"))),
                new eu.dissco.core.translator.schema.Identifications()
                    .withDwcIdentificationVerificationStatus(true)
                    .withTaxonIdentifications(
                        List.of(new TaxonIdentification().withDwcKingdom(kingdom)))
            )
        );
  }

  @ParameterizedTest
  @MethodSource("arguments")
  void testRetrieveFromDWCA(eu.dissco.core.translator.schema.DigitalSpecimen ds,
      OdsTopicDiscipline expected) {

    // When
    var result = topicDiscipline.calculate(ds);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testGetTerm() {
    // When
    var result = topicDiscipline.getTerm();

    // Then
    assertThat(result).isEqualTo(TopicDiscipline.TERM);
  }
}
