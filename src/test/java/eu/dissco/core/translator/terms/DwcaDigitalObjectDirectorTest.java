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
class DwcaDigitalObjectDirectorTest {

  @Mock
  private TermMapper termMapper;
  @Mock
  private RorComponent rorComponent;
  @Mock
  private WebClientProperties webClientProperties;
  @Mock
  private FdoProperties fdoProperties;

  private DwcaDigitalObjectDirector director;

  @BeforeEach
  void setup() {
    director = new DwcaDigitalObjectDirector(MAPPER, termMapper, rorComponent, webClientProperties,
        fdoProperties);
  }

  @Test
  void testConstructDwcaDigitalSpecimen() throws Exception {
    // Given
    var specimenJson = givenDwcaSpecimenJson();
    given(rorComponent.getRorName(anyString())).willReturn("National Museum of Natural History");
    given(termMapper.retrieveTerm(any(Term.class), eq(specimenJson), eq(true))).willReturn(
        "a mapped term");
    given(termMapper.retrieveTerm(any(OrganisationId.class), eq(specimenJson), eq(true)))
        .willReturn("https://ror.org/0443cwa12");
    given(termMapper.retrieveTerm(any(PhysicalSpecimenIdType.class), eq(specimenJson), eq(true)))
        .willReturn(OdsPhysicalSpecimenIdType.LOCAL.value());

    // When
    var result = director.assembleDigitalSpecimenTerm(specimenJson, true);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getEntityRelationships()).asList().hasSize(3);
    assertThat(result.getIdentifiers()).asList().hasSize(3);
    assertThat(result.getCitations()).asList().hasSize(2);
    assertThat(result.getDwcIdentification()).asList().hasSize(2);
  }

  @Test
  void testConstructDwcaDigitalMediaObject() throws JsonProcessingException, OrganisationNotRorId {
    // Given
    var specimenJson = givenDwcaMediaObject();
    given(rorComponent.getRorName(anyString())).willReturn("National Museum of Natural History");
    given(termMapper.retrieveTerm(any(Term.class), eq(specimenJson), eq(true))).willReturn(
        "a mapped term");

    // When
    var result = director.assembleDigitalMediaObjects(true, specimenJson,
        "https://ror.org/0443cwa12");

    // Then
    assertThat(result).isNotNull();
    assertThat(result.getEntityRelationships()).asList().hasSize(3);
    assertThat(result.getIdentifiers()).asList().hasSize(2);
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

}
