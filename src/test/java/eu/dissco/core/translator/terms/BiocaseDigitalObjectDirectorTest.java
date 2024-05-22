package eu.dissco.core.translator.terms;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.dissco.core.translator.component.InstitutionNameComponent;
import eu.dissco.core.translator.exception.UnknownPhysicalSpecimenIdType;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsPhysicalSpecimenIdType;
import eu.dissco.core.translator.terms.specimen.OrganisationId;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenIdType;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BiocaseDigitalObjectDirectorTest {

  @Mock
  private TermMapper termMapper;
  @Mock
  private InstitutionNameComponent institutionNameComponent;
  @Mock
  private WebClientProperties webClientProperties;
  @Mock
  private FdoProperties fdoProperties;

  private BiocaseDigitalObjectDirector director;

  private static Stream<Arguments> givenIdentifications() throws JsonProcessingException {
    return Stream.of(
        Arguments.of(addSecondIdentification(identifications(true)), 2),
        Arguments.of(identifications(true), 1),
        Arguments.of(identifications(false), 1),
        Arguments.of(MAPPER.createObjectNode(), 0)
    );
  }

  private static ObjectNode identifications(boolean verified) throws JsonProcessingException {
    var node = (ObjectNode) MAPPER.readTree(
        """
                {
                "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName": "Bryozoa",
                "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank": "Phylum",
                "abcd:identifications/identification/0/result/taxonIdentified/scientificName/fullScientificNameString": "Hemiphragma rotundatum",
                "abcd:identifications/identification/0/result/taxonIdentified/scientificName/nameAtomised/zoological/speciesEpithet": "rotundatum",
                "abcd:identifications/identification/0/identifiers/identifier/0/personName/fullName": "Ernst",
                "abcd:identifications/identification/0/identifiers/identifier/0/personName/atomisedName/inheritedName": "Ernst",
                "abcd:identifications/identification/0/identifiers/identifier/0/personName/atomisedName/givenNames": "Andrej"
                }
            """);
    if (verified) {
      node.put("abcd:identifications/identification/0/preferredFlag", true);
    }
    return node;
  }

  private static ObjectNode addSecondIdentification(ObjectNode identifications)
      throws JsonProcessingException {
    var node = (ObjectNode) MAPPER.readTree(
        """
                {
                "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName": "Bryozoa",
                "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank": "Phylum",
                "abcd:identifications/identification/1/result/taxonIdentified/scientificName/fullScientificNameString": "Hemiphragma rotundatum",
                "abcd:identifications/identification/1/result/taxonIdentified/scientificName/nameAtomised/zoological/speciesEpithet": "rotundatum",
                "abcd:identifications/identification/1/identifiers/identifier/0/personName/fullName": "Ernst",
                "abcd:identifications/identification/1/identifiers/identifier/0/personName/atomisedName/inheritedName": "Ernst",
                "abcd:identifications/identification/1/identifiers/identifier/0/personName/atomisedName/givenNames": "Andrej"
                }
            """);
    identifications.setAll(node);
    return identifications;
  }

  @BeforeEach
  void setup() {
    director = new BiocaseDigitalObjectDirector(MAPPER, termMapper, institutionNameComponent,
        webClientProperties,
        fdoProperties);
  }

  @ParameterizedTest
  @MethodSource("givenIdentifications")
  void testConstructAbcdDigitalSpecimen(ObjectNode identifications, int totalIdentifications)
      throws Exception {
    // Given
    var specimenJson = givenAbcdSpecimenJson(identifications);
    given(institutionNameComponent.getWikiDataName(anyString())).willReturn(
        "Tallinn University of Technology");
    given(termMapper.retrieveTerm(any(Term.class), eq(specimenJson), eq(false))).willReturn(
        "a mapped term");
    given(termMapper.retrieveTerm(any(OrganisationId.class), eq(specimenJson), eq(false)))
        .willReturn("https://www.wikidata.org/wiki/Q604487");
    given(termMapper.retrieveTerm(any(PhysicalSpecimenIdType.class), eq(specimenJson), eq(false)))
        .willReturn(OdsPhysicalSpecimenIdType.RESOLVABLE.value());

    // When
    var result = director.assembleDigitalSpecimenTerm(specimenJson, false);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getEntityRelationship()).asList().hasSize(4);
    assertThat(result.getIdentifier()).asList().hasSize(4);
    assertThat(result.getCitation()).asList().hasSize(1);
    assertThat(result.getDwcIdentification()).asList().hasSize(totalIdentifications);
  }

  @ParameterizedTest
  @ValueSource(strings = {"An Unknown PhysicalSpecimenIdType"})
  @NullSource
  void testConstructAbcdDigitalSpecimenUnknownIdType(String value) throws Exception {
    // Given
    var specimenJson = givenAbcdSpecimenJson(identifications(true));
    given(termMapper.retrieveTerm(any(PhysicalSpecimenIdType.class), eq(specimenJson), eq(false)))
        .willReturn(value);

    // When / Then
    assertThrowsExactly(UnknownPhysicalSpecimenIdType.class,
        () -> director.assembleDigitalSpecimenTerm(specimenJson, false));
  }

  private JsonNode givenAbcdSpecimenJson(ObjectNode identifications)
      throws JsonProcessingException {
    var node = (ObjectNode) MAPPER.readTree(
        """
            {
                  "abcd:unitGUID": "https://geocollections.info/specimen/287",
                  "abcd:sourceInstitutionID": "Department of Geology, TalTech",
                  "abcd:sourceID": "GIT",
                  "abcd:unitID": "155-287",
                  "abcd:unitIDNumeric": 287,
                  "abcd:dateLastEdited": 1649859975000,
                  "abcd:unitReferences/unitReference/0/titleCitation": "Ernst, 2022",
                  "abcd:unitReferences/unitReference/0/citationDetail": "Bryozoan fauna from the Kunda Stage (Darriwilian, Middle Ordovician) of Estonia and NW Russia",
                  "abcd:unitReferences/unitReference/0/uri": "10.3140/bull.geosci.1843",
                  "abcd:recordBasis": "FossilSpecimen",
                  "abcd:kindOfUnit/0/value": "specimen in parts",
                  "abcd:kindOfUnit/0/language": "en",
                  "abcd:gathering/dateTime/dateText": "1957",
                  "abcd:gathering/agents/gatheringAgent/0/agentText": "Rubel, Madis",
                  "abcd:gathering/agents/gatheringAgent/0/person/fullName": "Rubel, Madis",
                  "abcd:gathering/agents/gatheringAgent/0/person/atomisedName/inheritedName": "Rubel",
                  "abcd:gathering/agents/gatheringAgent/0/person/atomisedName/givenNames": "Madis",
                  "abcd:gathering/localityText/value": "Väike-Pakri cliff",
                  "abcd:gathering/localityText/language": "en",
                  "abcd:gathering/country/name/value": "Estonia",
                  "abcd:gathering/country/iso3166Code": "EE",
                  "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinateMethod": "Est Land Board map server",
                  "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/longitudeDecimal": 23.978771,
                  "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/latitudeDecimal": 59.357624,
                  "abcd:gathering/siteCoordinateSets/siteCoordinates/0/coordinatesLatLong/spatialDatum": "WGS84",
                  "abcd:gathering/stratigraphy/chronostratigraphicTerms/chronostratigraphicTerm/0/term": "Kunda Stage",
                  "abcd:gathering/stratigraphy/chronostratigraphicTerms/chronostratigraphicTerm/0/language": "en",
                  "abcd:gathering/stratigraphy/chronostratigraphicTerms/chronostratigraphicTerm/1/term": "Darriwilian",
                  "abcd:gathering/stratigraphy/chronostratigraphicTerms/chronostratigraphicTerm/1/language": "en",
                  "abcd:gathering/stratigraphy/lithostratigraphicTerms/lithostratigraphicTerm/0/term": "Pakri Formation",
                  "abcd:gathering/stratigraphy/lithostratigraphicTerms/lithostratigraphicTerm/0/language": "en",
                  "abcd:gathering/stratigraphy/stratigraphyText/value": "b",
                  "abcd:recordURI": "https://geocollections.info/specimen/287",
                  "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/0/chronoStratigraphicDivision": "Stage",
                  "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/chronostratigraphicAttributions/chronostratigraphicAttribution/0/chronostratigraphicName": "Kunda Stage",
                  "abcd-efg:earthScienceSpecimen/unitStratigraphicDetermination/lithostratigraphicAttributions/lithostratigraphicAttribution/0/formation": "Pakri Formation",
                  "abcd:description/representation/0/title": "TalTech geological collections",
                  "abcd:description/representation/0/details": "Geological and paleontological collections housed at the Institute of Geology at Tallinn University of Technology (formerly an institute of the Estonian Academy of Sciences) are the largest of their kind in Estonia. The particular strength of the collection lies in Ordovician and Silurian invertebrates and Devonian fish fossils of the Baltica paleocontinent. Most of the ca 300000 specimens are collected since the mid 20th century, few collections date back to 1850s.",
                  "abcd:description/representation/0/coverage": "Invertebrate and early vertebrate fossils, and sedimentary rocks primarily from the Paleozoic strata of Baltic Region and former Soviet Union; Quaternary sediments and fossils from Estonia; minerals and meteorites of worldwide coverage.",
                  "abcd:description/representation/0/uri": "https://geocollections.info",
                  "abcd:description/representation/0/language": "en",
                  "abcd:revisionData/dateModified": 1604528959000,
                  "abcd:owners/owner/0/organisation/name/representation/0/text": "Department of Geology, TalTech",
                  "abcd:owners/owner/0/organisation/name/representation/0/abbreviation": "GIT",
                  "abcd:owners/owner/0/organisation/name/representation/0/language": "en",
                  "abcd:owners/owner/0/person/fullName": "Ursula Toom",
                  "abcd:owners/owner/0/roles/role/0/value": "Chief Curator",
                  "abcd:owners/owner/0/addresses/address/0/value": "Ehitajate 5, 19086 Tallinn, Estonia",
                  "abcd:owners/owner/0/emailAddresses/emailAddress/0/value": "ursula.toom@taltech.ee",
                  "abcd:owners/owner/0/logoURI": "https://files.geocollections.info/img/geocase/taltech1.png",
                  "abcd:iprstatements/copyrights/copyright/0/text": "© Department of Geology, TalTech",
                  "abcd:iprstatements/copyrights/copyright/0/language": "en",
                  "abcd:iprstatements/licenses/license/0/text": "(CC) BY-NC",
                  "abcd:iprstatements/licenses/license/0/details": "Creative Commons Attribution-NonCommercial 4.0 Unported License",
                  "abcd:iprstatements/licenses/license/0/uri": "http://creativecommons.org/licenses/by-nc/4.0/",
                  "abcd:iprstatements/licenses/license/0/language": "en",
                  "abcd:iprstatements/termsOfUseStatements/termsOfUse/0/text": "Free for non-commercial usage, provided that Department of Geology at TalTech and https://geocollections.info are acknowledged.",
                  "abcd:iprstatements/termsOfUseStatements/termsOfUse/0/language": "en",
                  "abcd:iprstatements/disclaimers/disclaimer/0/text": "We make efforts to ensure that the data we serve are accurate. However, quality and completeness of every data record cannot be guaranteed. Use the data at your own responsibility.",
                  "abcd:iprstatements/disclaimers/disclaimer/0/language": "en",
                  "abcd:iprstatements/acknowledgements/acknowledgement/0/text": "National geological collection of Estonia; Department of Geology, TalTech",
                  "abcd:iprstatements/acknowledgements/acknowledgement/0/language": "en",
                  "abcd:iprstatements/citations/citation/text": "National geological collection of Estonia; Department of Geology, TalTech",
                  "abcd:iprstatements/citations/citation/language": "en"
                }
            """);
    node.setAll(identifications);
    return node;
  }

}
