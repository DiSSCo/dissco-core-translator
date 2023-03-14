package eu.dissco.core.translator.terms.specimen.stratigraphy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.dissco.core.translator.terms.Term;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Bed;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Formation;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Group;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Member;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LithostratigraphicTest {

  private static Stream<Arguments> dwcArguments() {
    return Stream.of(
        Arguments.of(new Bed(), "Harlem coal", "dwc:bed"),
        Arguments.of(new Formation(), "Fillmore Formation", "dwc:formation"),
        Arguments.of(new Group(), "Hellnmaria Member", "dwc:group"),
        Arguments.of(new Member(), "Bathurst", "dwc:member")
    );
  }

  private static Stream<Arguments> getTermArguments() {
    return Stream.of(
        Arguments.of(new Group(), "dwc:group"),
        Arguments.of(new Bed(), "dwc:bed"),
        Arguments.of(new Formation(), "dwc:formation"),
        Arguments.of(new Member(), "dwc:member")
    );
  }

  private static Stream<Arguments> abcdArguments() {
    return Stream.of(
        Arguments.of(new Bed(), "Harlem coal",
            List.of(Pair.of(
                "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/lithostratigraphicAttributions/lithostratigraphicAttribution/0/bed",
                "Harlem coal"))),
        Arguments.of(new Formation(), "Fillmore Formation",
            List.of(Pair.of(
                "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/lithostratigraphicAttributions/lithostratigraphicAttribution/0/formation",
                "Fillmore Formation"))),
        Arguments.of(new Group(), "Hellnmaria Member",
            List.of(Pair.of(
                "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/lithostratigraphicAttributions/lithostratigraphicAttribution/0/group",
                "Hellnmaria Member"))),
        Arguments.of(new Member(), "Bathurst",
            List.of(Pair.of(
                "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/lithostratigraphicAttributions/lithostratigraphicAttribution/0/member",
                "Bathurst"))
        )

    );
  }

  @ParameterizedTest
  @MethodSource("abcdArguments")
  void testRetrieveFromABCD(Term term, String expected, List<Pair<String, String>> arguments) {
    // Given
    var unit = MAPPER.createObjectNode();
    for (var argument : arguments) {
      unit.put(argument.getLeft(), argument.getRight());
    }

    // When
    var result = term.retrieveFromABCD(unit);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("dwcArguments")
  void testRetrieveFromDWCA(Term term, String expected, String dwc) {
    // Given
    var unit = new ObjectMapper().createObjectNode();
    unit.put(dwc, expected);

    // When
    var result = term.retrieveFromDWCA(unit);

    // Then
    assertThat(result).isEqualTo(expected);
  }

  @ParameterizedTest
  @MethodSource("getTermArguments")
  void testGetTerm(Term term, String expected) {
    // When
    var result = term.getTerm();

    // Then
    assertThat(result).isEqualTo(expected);
  }

}
