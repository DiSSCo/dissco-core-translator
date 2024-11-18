package eu.dissco.core.translator.terms;

import static eu.dissco.core.translator.TestUtils.MAPPER;
import static eu.dissco.core.translator.TestUtils.SOURCE_SYSTEM_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import eu.dissco.core.translator.component.OrganisationNameComponent;
import eu.dissco.core.translator.component.SourceSystemComponent;
import eu.dissco.core.translator.exception.OrganisationException;
import eu.dissco.core.translator.properties.FdoProperties;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsPhysicalSpecimenIDType;
import eu.dissco.core.translator.schema.DigitalSpecimen.OdsStatus;
import eu.dissco.core.translator.terms.specimen.OrganisationID;
import eu.dissco.core.translator.terms.specimen.PhysicalSpecimenIDType;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DwcaDigitalObjectDirectorTest {

  @Mock
  private TermMapper termMapper;
  @Mock
  private OrganisationNameComponent institutionNameComponent;
  @Mock
  private SourceSystemComponent sourceSystemComponent;
  @Mock
  private FdoProperties fdoProperties;

  private DwcaDigitalObjectDirector director;

  @BeforeEach
  void setup() {
    director = new DwcaDigitalObjectDirector(MAPPER, termMapper, institutionNameComponent,
        sourceSystemComponent, fdoProperties);
    given(sourceSystemComponent.getSourceSystemID()).willReturn(SOURCE_SYSTEM_ID);
  }

  @Test
  void testConstructDwcaDigitalSpecimen() throws Exception {
    // Given
    var specimenJson = givenDwcaSpecimenJson();
    given(institutionNameComponent.getRorName(anyString())).willReturn(
        "National Museum of Natural History");
    given(termMapper.retrieveTerm(any(Term.class), eq(specimenJson), eq(true))).willReturn(
        "a mapped term");
    given(termMapper.retrieveTerm(any(OrganisationID.class), eq(specimenJson), eq(true)))
        .willReturn("https://ror.org/0443cwa12");
    given(termMapper.retrieveTerm(any(PhysicalSpecimenIDType.class), eq(specimenJson), eq(true)))
        .willReturn(OdsPhysicalSpecimenIDType.LOCAL.value());

    // When
    var result = director.assembleDigitalSpecimenTerm(specimenJson, true);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getOdsStatus()).isEqualTo(OdsStatus.ACTIVE);
    assertThat(result.getOdsHasEntityRelationships()).asInstanceOf(InstanceOfAssertFactories.LIST)
        .hasSize(3);
    assertThat(result.getOdsHasIdentifiers()).asInstanceOf(InstanceOfAssertFactories.LIST)
        .hasSize(3);
    assertThat(result.getOdsHasCitations()).asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(2);
    assertThat(result.getOdsHasIdentifications()).asInstanceOf(InstanceOfAssertFactories.LIST)
        .hasSize(2);
  }

  @Test
  void testConstructDwcaDigitalSpecimenSingleIdentification() throws Exception {
    // Given
    var specimenJson = givenDwcaSpecimenSingleIdentificationJson();
    given(institutionNameComponent.getRorName(anyString())).willReturn(
        "National Museum of Natural History");
    given(termMapper.retrieveTerm(any(Term.class), eq(specimenJson), eq(true))).willReturn(
        "a mapped term");
    given(termMapper.retrieveTerm(any(OrganisationID.class), eq(specimenJson), eq(true)))
        .willReturn("https://ror.org/0443cwa12");
    given(termMapper.retrieveTerm(any(PhysicalSpecimenIDType.class), eq(specimenJson), eq(true)))
        .willReturn(OdsPhysicalSpecimenIDType.LOCAL.value());

    // When
    var result = director.assembleDigitalSpecimenTerm(specimenJson, true);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getOdsHasEntityRelationships()).asInstanceOf(InstanceOfAssertFactories.LIST)
        .hasSize(3);
    assertThat(result.getOdsHasIdentifiers()).asInstanceOf(InstanceOfAssertFactories.LIST)
        .hasSize(3);
    assertThat(result.getOdsHasCitations()).asInstanceOf(InstanceOfAssertFactories.LIST).hasSize(1);
    assertThat(result.getOdsHasIdentifications()).asInstanceOf(InstanceOfAssertFactories.LIST)
        .hasSize(1);
    assertThat(((eu.dissco.core.translator.schema.Identification) result.getOdsHasIdentifications()
        .get(0)).getOdsIsVerifiedIdentification()).isTrue();
  }


  @Test
  void testConstructDwcaDigitalMedia() throws JsonProcessingException, OrganisationException {
    // Given
    var specimenJson = givenDwcaMediaObject();
    given(institutionNameComponent.getRorName(anyString())).willReturn(
        "National Museum of Natural History");
    given(termMapper.retrieveTerm(any(Term.class), eq(specimenJson), eq(true))).willReturn(
        "a mapped term");

    // When
    var result = director.assembleDigitalMedia(true, specimenJson,
        "https://ror.org/0443cwa12");

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getOdsHasEntityRelationships()).asInstanceOf(InstanceOfAssertFactories.LIST)
        .hasSize(3);
    assertThat(result.getOdsHasIdentifiers()).asInstanceOf(InstanceOfAssertFactories.LIST)
        .hasSize(2);
  }

  private JsonNode givenDwcaMediaObject() throws JsonProcessingException {
    return MAPPER.readTree("""
              {
                        "dwca:ID": "http://coldb.mnhn.fr/catalognumber/mnhn/ec/ec12801",
                        "dcterms:type": "StillImage",
                        "dcterms:format": "image/jpeg",
                        "dcterms:creator": "Antoine Mantilleri",
                        "dcterms:license": "cc-by-nc-nd",
                        "dcterms:identifier": "https://mediaphoto.mnhn.fr/media/1622116345730PeXvxZIhEfm1vcVV"
                      }
        """);
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

  private JsonNode givenDwcaSpecimenSingleIdentificationJson() throws JsonProcessingException {
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
                    "dwc:scientificNameAuthorship": "Moravec, 2003"
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

}
