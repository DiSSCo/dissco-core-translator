package eu.dissco.core.translator.terms.specimen.stratigraphy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import eu.dissco.core.translator.terms.Term;
import eu.dissco.core.translator.terms.specimen.stratigraphy.biostratigraphic.HighestBiostratigraphicZone;
import eu.dissco.core.translator.terms.specimen.stratigraphy.biostratigraphic.LowestBiostratigraphicZone;
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
class BiostratigraphicZoneTest {

  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  private static Stream<Arguments> dwcArguments() {
    return Stream.of(
        Arguments.of(new HighestBiostratigraphicZone(), "Blancan",
            "dwc:highestBiostratigraphicZone", DwcTerm.highestBiostratigraphicZone),
        Arguments.of(new LowestBiostratigraphicZone(), "Maastrichtian",
            "dwc:lowestBiostratigraphicZone", DwcTerm.lowestBiostratigraphicZone)
    );
  }

  private static Stream<Arguments> getTermArguments() {
    return Stream.of(
        Arguments.of(new HighestBiostratigraphicZone(), "dwc:highestBiostratigraphicZone"),
        Arguments.of(new LowestBiostratigraphicZone(), "dwc:lowestBiostratigraphicZone")
    );
  }

  private static Stream<Arguments> abcdArguments() {
    return Stream.of(
        Arguments.of(new HighestBiostratigraphicZone(), "Type | ZoneName | SubZoneName",
            List.of(Pair.of(
                    "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/biostratigraphicAttributionsType/biostratigraphicAttribution/0/zonalFossilType",
                    "Type"),
                Pair.of(
                    "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/biostratigraphicAttributionsType/biostratigraphicAttribution/0/fossilZoneName",
                    "ZoneName"),
                Pair.of(
                    "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/biostratigraphicAttributionsType/biostratigraphicAttribution/0/fossilSubzoneName",
                    "SubZoneName")
            )),
        Arguments.of(new LowestBiostratigraphicZone(), "Type | ZoneName | SubZoneName",
            List.of(Pair.of(
                    "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/biostratigraphicAttributionsType/biostratigraphicAttribution/0/zonalFossilType",
                    "Type"),
                Pair.of(
                    "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/biostratigraphicAttributionsType/biostratigraphicAttribution/0/fossilZoneName",
                    "ZoneName"),
                Pair.of(
                    "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/biostratigraphicAttributionsType/biostratigraphicAttribution/0/fossilSubzoneName",
                    "SubZoneName")
            ))
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
