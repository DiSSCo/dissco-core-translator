package eu.dissco.core.translator.component;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.loadResourceFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import eu.dissco.core.translator.client.RorClient;
import eu.dissco.core.translator.client.WikidataClient;
import eu.dissco.core.translator.exception.OrganisationException;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganisationNameComponentTest {

  private static final String ROR = "03srysw20";
  private static final String WIKIDATA_ID = "Q2203052";
  @Mock
  private RorClient rorClient;
  @Mock
  private WikidataClient wikidataClient;
  private OrganisationNameComponent rorComponent;

  private static Stream<Arguments> badRorResponse() {
    return Stream.of(
        Arguments.of(
            """
                {
                  "names":"De MuseumFabriek"
                }
                """)
        ,
        Arguments.of("""
            {
              "names": ["De MuseumFabriek"]
            }
            """
        )
    );
  }

  @BeforeEach
  void setup() {
    this.rorComponent = new OrganisationNameComponent(rorClient, wikidataClient);
  }

  @Test
  void testGetRorId() throws Exception {
    // Given
    given(rorClient.getRorInformation(ROR)).willReturn(MAPPER.readTree(
        loadResourceFile("organisation-name/example-ror.json")));

    // When
    var result = rorComponent.getRorName(ROR);

    // Then
    assertThat(result).isEqualTo("The MuseumFactory");
  }

  @Test
  void testGetRorIdFirst() throws Exception {
    // Given
    given(rorClient.getRorInformation(ROR)).willReturn(MAPPER.readTree("""
        {
          "names": [
            {
              "lang": "nl",
              "types": [
                "label"
                ],
              "value": "De MuseumFabriek"
              }
          ]
        }
        """));

    // When
    var result = rorComponent.getRorName(ROR);

    // Then
    assertThat(result).isEqualTo("De MuseumFabriek");
  }

  @ParameterizedTest
  @MethodSource("badRorResponse")
  void testBadRorResponse(String rorResponse) {
    // Given
    given(rorClient.getRorInformation(ROR)).willReturn(MAPPER.readTree(rorResponse));

    // When / Then
    assertThrows(OrganisationException.class, () -> rorComponent.getRorName(ROR));

  }

  @Test
  void testWikidataId() throws Exception {
    // Given
    given(wikidataClient.getWikidataLabel(WIKIDATA_ID)).willReturn(
        MAPPER.readTree(loadResourceFile("organisation-name/example-wikidata.json")));

    // When
    var result = rorComponent.getWikiDataName(WIKIDATA_ID);

    // Then
    assertThat(result).isEqualTo("De Museumfabriek");
  }

  @Test
  void testWikidataIdInvalid() throws Exception {
    // Given
    given(wikidataClient.getWikidataLabel(WIKIDATA_ID)).willReturn(
        MAPPER.readTree(loadResourceFile("organisation-name/invalid-wikidata.json")));

    // When / Then
    assertThrows(OrganisationException.class, () -> rorComponent.getWikiDataName(WIKIDATA_ID));
  }

  @Test
  void testResponseInvalid() throws Exception {
    // Given
    given(rorClient.getRorInformation(ROR)).willReturn(
        MAPPER.readTree(loadResourceFile("organisation-name/response-invalid.json")));

    // When / Then
    assertThrows(OrganisationException.class, () -> rorComponent.getRorName(ROR));
  }

  @Test
  void testEmptyMono() {
    // Given
    given(rorClient.getRorInformation(ROR)).willReturn(MAPPER.createObjectNode());

    // When / Then
    assertThrows(OrganisationException.class, () -> rorComponent.getRorName(ROR));
  }
}
