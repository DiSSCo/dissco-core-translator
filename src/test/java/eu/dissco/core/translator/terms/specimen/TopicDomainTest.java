package eu.dissco.core.translator.terms.specimen;

import static org.assertj.core.api.Assertions.assertThat;

import eu.dissco.core.translator.schema.DigitalSpecimen.OdsTopicDiscipline;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsTopicDomain;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TopicDomainTest {
  private final TopicDomain topicDomain = new TopicDomain();

  private static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of(givenDigitalSpecimen("FossilSpecimen"), OdsTopicDomain.LIFE),
        Arguments.of(givenDigitalSpecimen("MeteoriteSpecimen"), OdsTopicDomain.EXTRATERRESTRIAL),
        Arguments.of(givenDigitalSpecimen("RockSpecimen"), OdsTopicDomain.EARTH_SYSTEM),
        Arguments.of(givenDigitalSpecimen("PreservedSpecimen"), OdsTopicDomain.LIFE),
        Arguments.of(givenDigitalSpecimen("Other"), OdsTopicDomain.UNCLASSIFIED)
    );
  }

  private static Object givenDigitalSpecimen(String basisOfRecord) {
    return new eu.dissco.core.translator.schema.DigitalSpecimen()
        .withDwcBasisOfRecord(basisOfRecord);
  }

  @ParameterizedTest
  @MethodSource("arguments")
  void testRetrieveFromDWCA(eu.dissco.core.translator.schema.DigitalSpecimen ds,
      OdsTopicDomain expected) {

    // When
    var result = topicDomain.calculate(ds);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void testGetTerm() {
    // When
    var result = topicDomain.getTerm();

    // Then
    assertThat(result).isEqualTo(TopicDomain.TERM);
  }
}
