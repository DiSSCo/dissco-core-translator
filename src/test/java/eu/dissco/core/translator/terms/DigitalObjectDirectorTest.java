package eu.dissco.core.translator.terms;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.component.RorComponent;
import eu.dissco.core.translator.exception.OrganisationNotRorId;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsPhysicalSpecimenIdType;
import eu.dissco.core.translator.terms.specimen.LivingOrPreserved;
import eu.dissco.core.translator.terms.specimen.TopicDiscipline;
import eu.dissco.core.translator.terms.specimen.occurence.OccurrenceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DigitalObjectDirectorTest {

  @Mock
  private TermMapper termMapper;
  @Mock
  private RorComponent rorComponent;

  private DigitalObjectDirector director;

  @BeforeEach
  void setup() {
    director = new DigitalObjectDirector(MAPPER, termMapper, rorComponent);
  }

  @Test
  void testConstructAbcdDigitalSpecimen() throws JsonProcessingException, OrganisationNotRorId {
    // Given
    var specimenJson = givenAbcdSpecimenJson();
    var ds = givenDigitalSpecimen();
    given(rorComponent.getRoRId(anyString())).willReturn("Tallinn University of Technology");
    given(termMapper.retrieveTerm(any(Term.class), eq(specimenJson), eq(false))).willReturn(
        "a mapped term");
    given(termMapper.retrieveTerm(any(TopicDiscipline.class), eq(specimenJson),
        eq(false))).willReturn("Unclassified");
    given(termMapper.retrieveTerm(any(LivingOrPreserved.class), eq(specimenJson),
        eq(false))).willReturn("present");
    given(termMapper.retrieveTerm(any(OccurrenceStatus.class), eq(specimenJson),
        eq(false))).willReturn("present");

    // When
    var result = director.constructDigitalSpecimen(ds, false, specimenJson);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getEntityRelationships()).asList().hasSize(3);
    assertThat(result.getIdentifiers()).asList().hasSize(4);
    assertThat(result.getCitations()).asList().hasSize(1);
    assertThat(result.getDwcIdentification()).asList().hasSize(1);
  }

  @Test
  void testConstructDwcaDigitalSpecimen() throws JsonProcessingException, OrganisationNotRorId {
    // Given
    var specimenJson = givenDwcaSpecimenJson();
    var ds = givenDigitalSpecimen();
    given(rorComponent.getRoRId(anyString())).willReturn("National Museum of Natural History");
    given(termMapper.retrieveTerm(any(Term.class), eq(specimenJson), eq(true))).willReturn(
        "a mapped term");
    given(
        termMapper.retrieveTerm(any(TopicDiscipline.class), eq(specimenJson), eq(true))).willReturn(
        "Unclassified");
    given(termMapper.retrieveTerm(any(LivingOrPreserved.class), eq(specimenJson),
        eq(true))).willReturn("present");
    given(termMapper.retrieveTerm(any(OccurrenceStatus.class), eq(specimenJson),
        eq(true))).willReturn("present");

    // When
    var result = director.constructDigitalSpecimen(ds, true, specimenJson);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getEntityRelationships()).asList().hasSize(3);
    assertThat(result.getIdentifiers()).asList().hasSize(3);
    assertThat(result.getCitations()).asList().hasSize(2);
    assertThat(result.getDwcIdentification()).asList().hasSize(2);
  }

  private eu.dissco.core.translator.schema.DigitalSpecimen givenDigitalSpecimen() {
    return new eu.dissco.core.translator.schema.DigitalSpecimen()
        .withOdsPhysicalSpecimenIdType(OdsPhysicalSpecimenIdType.RESOLVABLE)
        .withDwcInstitutionId("https://ror.org/0443cwa12");
  }

  private JsonNode givenDwcaSpecimenJson() throws JsonProcessingException {
    return MAPPER.readTree("""
        {
              "dwc:sex": "M",
              "dwca:ID": "http://coldb.mnhn.fr/catalognumber/mnhn/ec/ec10867",
              "dwc:genus": "Pogonostoma",
              "dwc:order": "Coleoptera",
              "dwc:county": "Boeny",
              "dwc:family": "Cicindelidae",
              "extensions": {
                "dwc:Identification": [
                  {
                    "dwca:ID": "http://coldb.mnhn.fr/catalognumber/mnhn/ec/ec10867",
                    "dwc:genus": "Pogonostoma",
                    "dwc:order": "Coleoptera",
                    "dwc:family": "Cicindelidae",
                    "dwc:taxonID": "ARTHROTER|145865",
                    "dwc:subgenus": "Pogonostoma",
                    "dwc:typeStatus": "paratype(s)",
                    "dwc:identifiedBy": "Moravec, J.",
                    "dwc:dateIdentified": "2007",
                    "dwc:scientificName": "Pogonostoma (Pogonostoma) subtiligrossum pacholatkoi Moravec, 2003",
                    "dwc:specificEpithet": "subtiligrossum",
                    "dwc:identificationID": "ARTHROTER|218826",
                    "dwc:namePublishedInYear": "2003",
                    "dwc:higherClassification": "Pogonostoma subtiligrossum;Pogonostoma;Cicindelidae;Coleoptera",
                    "dwc:infraspecificEpithet": "pacholatkoi",
                    "dwc:scientificNameAuthorship": "Moravec, 2003",
                    "dwc:identificationVerificationStatus": "1"
                  },
                  {
                    "dwca:ID": "http://coldb.mnhn.fr/catalognumber/mnhn/ec/ec10867",
                    "dwc:genus": "Pogonostoma",
                    "dwc:order": "Coleoptera",
                    "dwc:family": "Cicindelidae",
                    "dwc:taxonID": "ARTHROTER|145865",
                    "dwc:subgenus": "Pogonostoma",
                    "dwc:typeStatus": "paratype(s)",
                    "dwc:identifiedBy": "Moravec, J.",
                    "dwc:dateIdentified": "2003",
                    "dwc:scientificName": "Pogonostoma (Pogonostoma) subtiligrossum pacholatkoi Moravec, 2003",
                    "dwc:specificEpithet": "subtiligrossum",
                    "dwc:identificationID": "ARTHROTER|218827",
                    "dwc:namePublishedInYear": "2003",
                    "dwc:higherClassification": "Pogonostoma subtiligrossum;Pogonostoma;Cicindelidae;Coleoptera",
                    "dwc:infraspecificEpithet": "pacholatkoi",
                    "dwc:scientificNameAuthorship": "Moravec, 2003",
                    "dwc:identificationVerificationStatus": "0"
                  }
                ],
                "gbif:Reference": [
                  {
                    "dwca:ID": "http://coldb.mnhn.fr/catalognumber/mnhn/ec/ec10867",
                    "dcterms:date": "2003",
                    "dcterms:type": "publication",
                    "dcterms:title": "New or rare Madagascar tiger beetles-8.Some taxonomic results in the genus Pogonostoma (Coleoptera: Cicindelidae). Folia Heyrovskyana 11: 5-34.",
                    "dcterms:creator": "Moravec, J.",
                    "dwc:taxonRemarks": "Pogonostoma (Pogonostoma) subtiligrossum pacholatkoi Moravec, 2003",
                    "dcterms:bibliographicCitation": "Moravec, J., 2003 - New or rare Madagascar tiger beetles-8.Some taxonomic results in the genus Pogonostoma (Coleoptera: Cicindelidae). Folia Heyrovskyana 11: 5-34."
                  },
                  {
                    "dwca:ID": "http://coldb.mnhn.fr/catalognumber/mnhn/ec/ec10867",
                    "dcterms:date": "2007",
                    "dcterms:type": "publication",
                    "dcterms:title": "A Monograph of the Genus Pogonostoma (Coleoptera: Cicindelidae). Tiger Beetles of Madagascar volume 1. Zlin: Kabourek ed., 499 pp.",
                    "dcterms:creator": "Moravec, J.",
                    "dwc:taxonRemarks": "Pogonostoma (Pogonostoma) subtiligrossum pacholatkoi Moravec, 2003",
                    "dcterms:bibliographicCitation": "Moravec, J., 2007 - A Monograph of the Genus Pogonostoma (Coleoptera: Cicindelidae). Tiger Beetles of Madagascar volume 1. Zlin: Kabourek ed., 499 pp."
                  }
                ]
              },
              "dwc:country": "Madagascar",
              "dwc:locality": "Katsepy",
              "dwc:continent": "Afrique",
              "dwc:eventDate": "1997-12-24/1997-12-31",
              "dwc:recordedBy": "Moravec J. & Pacholátko P.",
              "dwc:typeStatus": "paratype(s)",
              "dcterms:modified": "2021-01-22 09:42:52.0",
              "dwc:identifiedBy": "Moravec, J.",
              "dwc:occurrenceID": "http://coldb.mnhn.fr/catalognumber/mnhn/ec/ec10867",
              "dwc:basisOfRecord": "PreservedSpecimen",
              "dwc:catalogNumber": "EC10867",
              "dwc:stateProvince": "Mahajanga",
              "dwc:collectionCode": "EC",
              "dwc:scientificName": "Pogonostoma (Pogonostoma) subtiligrossum pacholatkoi Moravec, 2003",
              "dwc:decimalLatitude": "-15.766667",
              "dwc:individualCount": "1",
              "dwc:institutionCode": "MNHN",
              "dwc:decimalLongitude": "46.233333",
              "dwc:verbatimLatitude": "-15.766667",
              "dwc:occurrenceRemarks": "Madagascar, Katsepy (Majunga), 24-31.XII. 1997 leg., Moravec et Pacholatko/ Paratype, Pogonostoma (s.str.), subtiligrossum, pacholatkoi ssp.n., det. J. Moravec 2002/ MNHN, EC10867",
              "dwc:verbatimLongitude": "46.233333",
              "dwc:scientificNameAuthorship": "Moravec, 2003"
            }
        """);
  }

  private JsonNode givenAbcdSpecimenJson() throws JsonProcessingException {
    return MAPPER.readTree(
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
                  "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName": "Bryozoa",
                  "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank": "Phylum",
                  "abcd:identifications/identification/0/result/taxonIdentified/scientificName/fullScientificNameString": "Hemiphragma rotundatum",
                  "abcd:identifications/identification/0/result/taxonIdentified/scientificName/nameAtomised/zoological/speciesEpithet": "rotundatum",
                  "abcd:identifications/identification/0/preferredFlag": true,
                  "abcd:identifications/identification/0/identifiers/identifier/0/personName/fullName": "Ernst",
                  "abcd:identifications/identification/0/identifiers/identifier/0/personName/atomisedName/inheritedName": "Ernst",
                  "abcd:identifications/identification/0/identifiers/identifier/0/personName/atomisedName/givenNames": "Andrej",
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
            """
    );
  }

}
