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
import eu.dissco.core.translator.component.RorComponent;
import eu.dissco.core.translator.exception.OrganisationNotRorId;
import eu.dissco.core.translator.exception.UnknownPhysicalSpecimenIdType;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.properties.WebClientProperties;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsPhysicalSpecimenIdType;
import eu.dissco.core.translator.terms.specimen.OrganisationId;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BiocaseDigitalObjectDirectorTest {

  @Mock
  private TermMapper termMapper;
  @Mock
  private RorComponent rorComponent;
  @Mock
  private WebClientProperties webClientProperties;
  @Mock
  private FdoProperties fdoProperties;

  private BiocaseDigitalObjectDirector director;

  @BeforeEach
  void setup() {
    director = new BiocaseDigitalObjectDirector(MAPPER, termMapper, rorComponent, webClientProperties,
        fdoProperties);
  }

  @Test
  void testConstructAbcdDigitalSpecimen() throws Exception {
    // Given
    var specimenJson = givenAbcdSpecimenJson();
    given(rorComponent.getRorName(anyString())).willReturn("Tallinn University of Technology");
    given(termMapper.retrieveTerm(any(Term.class), eq(specimenJson), eq(false))).willReturn(
        "a mapped term");
    given(termMapper.retrieveTerm(any(OrganisationId.class), eq(specimenJson), eq(false)))
        .willReturn("https://ror.org/0443cwa12");
    given(termMapper.retrieveTerm(any(PhysicalSpecimenIdType.class), eq(specimenJson), eq(false)))
        .willReturn(OdsPhysicalSpecimenIdType.RESOLVABLE.value());

    // When
    var result = director.assembleDigitalSpecimenTerm(specimenJson, false);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getEntityRelationships()).asList().hasSize(4);
    assertThat(result.getIdentifiers()).asList().hasSize(3);
    assertThat(result.getCitations()).asList().hasSize(0);
    assertThat(result.getDwcIdentification()).asList().hasSize(4);
  }

  @ParameterizedTest
  @ValueSource(strings = {"An Unknown PhysicalSpecimenIdType"})
  @NullSource
  void testConstructAbcdDigitalSpecimenUnknownIdType(String value) throws Exception {
    // Given
    var specimenJson = givenAbcdSpecimenJson();
    given(termMapper.retrieveTerm(any(PhysicalSpecimenIdType.class), eq(specimenJson), eq(false)))
        .willReturn(value);

    // When / Then
    assertThrowsExactly(UnknownPhysicalSpecimenIdType.class,
        () -> director.assembleDigitalSpecimenTerm(specimenJson, false));
  }

  private JsonNode givenAbcdSpecimenJson() throws JsonProcessingException {
    return MAPPER.readTree(
        """
          {
            "abcd:unitGUID": "https://herbarium.bgbm.org/object/BW10795010",
            "abcd:sourceInstitutionID": "B",
            "abcd:sourceID": "Herbarium Berolinense",
            "abcd:unitID": "B -W 10795 -01 0",
            "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName": "Plantae",
            "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank": "regnum",
            "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/1/higherTaxonName": "LABIATAE",
            "abcd:identifications/identification/0/result/taxonIdentified/higherTaxa/higherTaxon/1/higherTaxonRank": "familia",
            "abcd:identifications/identification/0/result/taxonIdentified/scientificName/fullScientificNameString": "Bystropogon reticulatus",
            "abcd:identifications/identification/0/result/taxonIdentified/scientificName/nameAtomised/botanical/genusOrMonomial": "Bystropogon",
            "abcd:identifications/identification/0/result/taxonIdentified/scientificName/nameAtomised/botanical/firstEpithet": "reticulatus",
            "abcd:identifications/identification/0/preferredFlag": false,
            "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName": "Plantae",
            "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank": "regnum",
            "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/1/higherTaxonName": "LABIATAE",
            "abcd:identifications/identification/1/result/taxonIdentified/higherTaxa/higherTaxon/1/higherTaxonRank": "familia",
            "abcd:identifications/identification/1/result/taxonIdentified/scientificName/fullScientificNameString": "Bystropogon mollis Kunth",
            "abcd:identifications/identification/1/result/taxonIdentified/scientificName/nameAtomised/botanical/genusOrMonomial": "Bystropogon",
            "abcd:identifications/identification/1/result/taxonIdentified/scientificName/nameAtomised/botanical/firstEpithet": "mollis",
            "abcd:identifications/identification/1/result/taxonIdentified/scientificName/nameAtomised/botanical/authorTeam": "Kunth",
            "abcd:identifications/identification/1/preferredFlag": false,
            "abcd:identifications/identification/2/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName": "Plantae",
            "abcd:identifications/identification/2/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank": "regnum",
            "abcd:identifications/identification/2/result/taxonIdentified/higherTaxa/higherTaxon/1/higherTaxonName": "LABIATAE",
            "abcd:identifications/identification/2/result/taxonIdentified/higherTaxa/higherTaxon/1/higherTaxonRank": "familia",
            "abcd:identifications/identification/2/result/taxonIdentified/scientificName/fullScientificNameString": "Minthostachys mollis (Kunth) Griseb.",
            "abcd:identifications/identification/2/result/taxonIdentified/scientificName/nameAtomised/botanical/genusOrMonomial": "Minthostachys",
            "abcd:identifications/identification/2/result/taxonIdentified/scientificName/nameAtomised/botanical/firstEpithet": "mollis",
            "abcd:identifications/identification/2/result/taxonIdentified/scientificName/nameAtomised/botanical/authorTeamParenthesis": "Kunth",
            "abcd:identifications/identification/2/result/taxonIdentified/scientificName/nameAtomised/botanical/authorTeam": "Griseb.",
            "abcd:identifications/identification/2/preferredFlag": false,
            "abcd:identifications/identification/3/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonName": "Plantae",
            "abcd:identifications/identification/3/result/taxonIdentified/higherTaxa/higherTaxon/0/higherTaxonRank": "regnum",
            "abcd:identifications/identification/3/result/taxonIdentified/higherTaxa/higherTaxon/1/higherTaxonName": "LABIATAE",
            "abcd:identifications/identification/3/result/taxonIdentified/higherTaxa/higherTaxon/1/higherTaxonRank": "familia",
            "abcd:identifications/identification/3/result/taxonIdentified/scientificName/fullScientificNameString": "Bystropogon reticulatus Willd. ex Steud.",
            "abcd:identifications/identification/3/result/taxonIdentified/scientificName/nameAtomised/botanical/genusOrMonomial": "Bystropogon",
            "abcd:identifications/identification/3/result/taxonIdentified/scientificName/nameAtomised/botanical/firstEpithet": "reticulatus",
            "abcd:identifications/identification/3/result/taxonIdentified/scientificName/nameAtomised/botanical/authorTeam": "Willd. ex Steud.",
            "abcd:identifications/identification/3/preferredFlag": true,
            "abcd:recordBasis": "PreservedSpecimen",
            "abcd:kindOfUnit/0/value": "herbarium sheet",
            "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typifiedName/fullScientificNameString": "Bystropogon mollis Kunth",
            "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/0/typeStatus": "isotype",
            "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/1/typifiedName/fullScientificNameString": "Bystropogon reticulatus",
            "abcd:specimenUnit/nomenclaturalTypeDesignations/nomenclaturalTypeDesignation/1/typeStatus": "type",
            "abcd:gathering/agents/gatheringAgentsText/value": "Bonpland,A.J.A. & Humboldt,F.W.H.A. v.",
            "abcd:gathering/localityText/value": "Ecuador: Leg.: A. J. A. Bonpland & F. W. H. A. v. Humboldt 3278.",
            "abcd:gathering/country/name/value": "Ecuador",
            "abcd:gathering/country/iso3166Code": "EC",
            "abcd:gathering/namedAreas/namedArea/0/areaClass/value": "Continent",
            "abcd:gathering/namedAreas/namedArea/0/areaClass/language": "en",
            "abcd:gathering/namedAreas/namedArea/0/areaName/value": "Middle and South America",
            "abcd:gathering/namedAreas/namedArea/0/areaName/language": "en",
            "abcd:gathering/biotope/text/value": "Ecuador.",
            "abcd:collectorsFieldNumber": "3278",
            "abcd:recordURI": "https://herbarium.bgbm.org/object/BW10795010",
            "abcd:description/representation/0/title": "Herbarium Berolinense, Berlin (B)",
            "abcd:description/representation/0/details": "The herbarium of the Botanic Garden and Botanical Museum Berlin-Dahlem (herbarium acronym: B) is the largest in Germany and holds a collection of more than 3.5 million preserved specimens. All plant groups – flowering plants, ferns, mosses, liverworts, and algae, as well as fungi and lichens – are represented in the collections which are worldwide in scope. Associated with the general herbarium are special collections of dried fruits and seeds, wood samples, and specimens preserved in alcohol. The collections of the herbarium are growing constantly through field research conducted by staff, and through gifts, acquisitions, and exchanges of specimens from other herbaria",
            "abcd:description/representation/0/language": "en",
            "abcd:iconURI": "http://ww3.bgbm.org/providerResources/BGBM.jpg",
            "abcd:revisionData/creators": "Herbarium Berolinense",
            "abcd:revisionData/dateCreated": 1054418400000,
            "abcd:revisionData/dateModified": 1465893720000,
            "abcd:owners/owner/0/organisation/name/representation/0/text": "Botanic Garden and Botanical Museum Berlin",
            "abcd:owners/owner/0/organisation/name/representation/0/abbreviation": "BGBM",
            "abcd:owners/owner/0/organisation/name/representation/0/language": "en",
            "abcd:owners/owner/0/addresses/address/0/value": "Königin-Luise-Str. 6-8, 14195 Berlin, Germany",
            "abcd:owners/owner/0/emailAddresses/emailAddress/0/value": "biodiversitydata@bgbm.org",
            "abcd:owners/owner/0/uris/url/0/value": "http://www.bgbm.org",
            "abcd:iprstatements/licenses/license/0/text": "Textual metadata on specimens from the Herbarium Berolinense (B) are released under the Creative Commons 1.0 Public Domain Dedication waiver [http://creativecommons.org/publicdomain/zero/1.0/].",
            "abcd:iprstatements/licenses/license/0/uri": "http://creativecommons.org/publicdomain/zero/1.0/",
            "abcd:iprstatements/licenses/license/0/language": "en",
            "abcd:iprstatements/termsOfUseStatements/termsOfUse/0/text": "Textual metadata on specimens from the Herbarium Berolinense (B) are released under the Creative Commons 1.0 Public Domain Dedication waiver [http://creativecommons.org/publicdomain/zero/1.0/].",
            "abcd:iprstatements/termsOfUseStatements/termsOfUse/0/language": "en",
            "abcd:iprstatements/citations/citation/text": "Curators Herbarium B (2000+). Digital specimen images at the Herbarium Berolinense. [Dataset]. Version: . Data Publisher: Botanic Garden and Botanical Museum Berlin. http://ww2.bgbm.org/herbarium/. [Please cite individual specimens with their stable ID, for images add the image ID.] For reference to a specimen cite the stable identifier as shown on the specimen detail page  (e.g. http://herbarium.bgbm.org/object/B200072053). For reference to an individual image, cite the stable identifier of the specimen as well as the image ID as shown on the image detail page",
            "abcd:iprstatements/citations/citation/language": "en"
          }
            """
    );
  }

}
