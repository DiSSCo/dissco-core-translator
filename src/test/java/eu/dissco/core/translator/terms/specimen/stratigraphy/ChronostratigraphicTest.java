package eu.dissco.core.translator.terms.specimen.stratigraphy;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import eu.dissco.core.translator.terms.Term;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestAgeOrLowestStage;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestEonOrLowestEonothem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestEpochOrLowestSeries;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestEraOrLowestErathem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.EarliestPeriodOrLowestSystem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestAgeOrHighestStage;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestEonOrHighestEonothem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestEpochOrHighestSeries;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestEraOrHighestErathem;
import eu.dissco.core.translator.terms.specimen.stratigraphy.chronostratigraphic.LatestPeriodOrHighestSystem;
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
class ChronostratigraphicTest {

  private static final String ABCD_DIVISION_0 = "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/0/chronoStratigraphicDivision";
  private static final String ABCD_NAME_0 = "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/0/chronostratigraphicName";
  private static final String ABCD_DIVISION_1 = "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/1/chronoStratigraphicDivision";
  private static final String ABCD_NAME_1 = "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/1/chronostratigraphicName";
  @Mock
  private ArchiveFile archiveFile;
  @Mock
  private Record rec;

  private static Stream<Arguments> dwcArguments() {
    return Stream.of(
        Arguments.of(new EarliestEpochOrLowestSeries(), "Öland Series",
            "dwc:earliestEpochOrLowestSeries", DwcTerm.earliestEpochOrLowestSeries),
        Arguments.of(new LatestEpochOrHighestSeries(), "Öland Series",
            "dwc:latestEpochOrHighestSeries", DwcTerm.latestEpochOrHighestSeries),
        Arguments.of(new EarliestAgeOrLowestStage(), "Pakerort Stage",
            "dwc:earliestAgeOrLowestStage", DwcTerm.earliestAgeOrLowestStage),
        Arguments.of(new LatestAgeOrHighestStage(), "Pakerort Stage",
            "dwc:latestAgeOrHighestStage", DwcTerm.latestAgeOrHighestStage),
        Arguments.of(new EarliestPeriodOrLowestSystem(), "Baltic Ordovician",
            "dwc:earliestPeriodOrLowestSystem", DwcTerm.earliestPeriodOrLowestSystem),
        Arguments.of(new LatestPeriodOrHighestSystem(), "Baltic Ordovician",
            "dwc:latestPeriodOrHighestSystem", DwcTerm.latestPeriodOrHighestSystem),
        Arguments.of(new EarliestEraOrLowestErathem(), "Mesozoic",
            "dwc:earliestEraOrLowestErathem", DwcTerm.earliestEraOrLowestErathem),
        Arguments.of(new LatestEraOrHighestErathem(), "Mesozoic",
            "dwc:latestEraOrHighestErathem", DwcTerm.latestEraOrHighestErathem),
        Arguments.of(new EarliestEonOrLowestEonothem(), "Phanerozoic",
            "dwc:earliestEonOrLowestEonothem", DwcTerm.earliestEonOrLowestEonothem),
        Arguments.of(new LatestEonOrHighestEonothem(), "Phanerozoic",
            "dwc:latestEonOrHighestEonothem", DwcTerm.latestEonOrHighestEonothem)
    );
  }

  private static Stream<Arguments> abcdArguments() {
    return Stream.of(
        Arguments.of(new EarliestAgeOrLowestStage(), "Pakerort SubStage",
            List.of(Pair.of(ABCD_DIVISION_0, "Stage"), Pair.of(ABCD_NAME_0, "Pakerort Stage"),
            Pair.of(ABCD_DIVISION_1, "SubStage"), Pair.of(ABCD_NAME_1, "Pakerort SubStage"))),
        Arguments.of(new LatestAgeOrHighestStage(), "Pakerort Stage",
            List.of(Pair.of(ABCD_DIVISION_0, "Stage"), Pair.of(ABCD_NAME_0, "Pakerort Stage"))),
        Arguments.of(new EarliestEpochOrLowestSeries(), "Öland Series",
            List.of(Pair.of(ABCD_DIVISION_0, "Series"), Pair.of(ABCD_NAME_0, "Öland Series"))),
        Arguments.of(new LatestEpochOrHighestSeries(), "Öland Series",
            List.of(Pair.of(ABCD_DIVISION_0, "Series"), Pair.of(ABCD_NAME_0, "Öland Series"))),
        Arguments.of(new EarliestPeriodOrLowestSystem(), "Baltic Ordovician",
            List.of(Pair.of(ABCD_DIVISION_0, "System"), Pair.of(ABCD_NAME_0, "Baltic Ordovician"))),
        Arguments.of(new LatestPeriodOrHighestSystem(), "Baltic Ordovician",
            List.of(Pair.of(ABCD_DIVISION_0, "System"), Pair.of(ABCD_NAME_0, "Baltic Ordovician"))),
        Arguments.of(new EarliestEraOrLowestErathem(), "Mesozoic",
            List.of(Pair.of(ABCD_DIVISION_0, "Erathem"), Pair.of(ABCD_NAME_0, "Mesozoic"))),
        Arguments.of(new LatestEraOrHighestErathem(), "Mesozoic",
            List.of(Pair.of(ABCD_DIVISION_0, "Erathem"), Pair.of(ABCD_NAME_0, "Mesozoic"))),
        Arguments.of(new EarliestEonOrLowestEonothem(), "Phanerozoic",
            List.of(Pair.of(ABCD_DIVISION_0, "Eonothem"), Pair.of(ABCD_NAME_0, "Phanerozoic"))),
        Arguments.of(new LatestEonOrHighestEonothem(), "Phanerozoic",
            List.of(Pair.of(ABCD_DIVISION_0, "Eonothem"), Pair.of(ABCD_NAME_0, "Phanerozoic"))),
        Arguments.of(new EarliestEonOrLowestEonothem(), null, List.of())
    );
  }

  private static Stream<Arguments> getTermArguments() {
    return Stream.of(
        Arguments.of(new EarliestAgeOrLowestStage(), "dwc:earliestAgeOrLowestStage"),
        Arguments.of(new LatestAgeOrHighestStage(), "dwc:latestAgeOrHighestStage"),
        Arguments.of(new EarliestEpochOrLowestSeries(), "dwc:earliestEpochOrLowestSeries"),
        Arguments.of(new LatestEpochOrHighestSeries(), "dwc:latestEpochOrHighestSeries"),
        Arguments.of(new EarliestPeriodOrLowestSystem(), "dwc:earliestPeriodOrLowestSystem"),
        Arguments.of(new LatestPeriodOrHighestSystem(), "dwc:latestPeriodOrHighestSystem"),
        Arguments.of(new EarliestEraOrLowestErathem(), "dwc:earliestEraOrLowestErathem"),
        Arguments.of(new LatestEraOrHighestErathem(), "dwc:latestEraOrHighestErathem"),
        Arguments.of(new EarliestEonOrLowestEonothem(), "dwc:earliestEonOrLowestEonothem"),
        Arguments.of(new LatestEonOrHighestEonothem(), "dwc:latestEonOrHighestEonothem")
    );
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
  @MethodSource("getTermArguments")
  void testGetTerm(Term term, String expected) {
    // When
    var result = term.getTerm();

    // Then
    assertThat(result).isEqualTo(expected);
  }

}
