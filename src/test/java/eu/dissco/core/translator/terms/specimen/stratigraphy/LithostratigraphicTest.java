package eu.dissco.core.translator.terms.specimen.stratigraphy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import eu.dissco.core.translator.terms.Term;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Bed;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Formation;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Group;
import eu.dissco.core.translator.terms.specimen.stratigraphy.lithostratigraphic.Member;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;
import org.gbif.dwc.ArchiveField;
import org.gbif.dwc.ArchiveFile;
import org.gbif.dwc.record.Record;
import org.gbif.dwc.terms.DwcTerm;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LithostratigraphicTest {

  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  private static Stream<Arguments> dwcArguments() {
    return Stream.of(
        Arguments.of(new Bed(), "Harlem coal", "dwc:bed", DwcTerm.bed),
        Arguments.of(new Formation(), "Fillmore Formation", "dwc:formation", DwcTerm.formation),
        Arguments.of(new Group(), "Hellnmaria Member", "dwc:group", DwcTerm.group),
        Arguments.of(new Member(), "Bathurst", "dwc:member", DwcTerm.member)
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
  void testRetrieveFromDWCA(Term term, String expected, String dwc,
      org.gbif.dwc.terms.Term dwcTerm) {
    // Given
    var archiveField = new ArchiveField(0, dwcTerm);
    given(archiveFile.getField(dwc)).willReturn(archiveField);
    given(rec.value(archiveField.getTerm())).willReturn(expected);

    // When
    var result = term.retrieveFromDWCA(archiveFile, rec);

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
